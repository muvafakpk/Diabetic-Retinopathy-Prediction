from django.shortcuts import render
from django.conf import settings
# Create your views here.
import datetime
import smtplib
from datetime import timedelta
import os
from email.mime.text import MIMEText
from django.http import HttpResponse, request, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.files.storage import FileSystemStorage
from django.db.models import Q
from django.db.models.expressions import RawSQL

from django.shortcuts import render, redirect

# Create your views here.
from myapp.models import *

e_email="careplus@gmail.com"
emailpassword="cmkk ggxb otdm ghmf"


def default(request):
    return render(request, 'index.html')


def login(request):
    return render(request, 'Admin login.html')


def login_post(request):
    user = request.POST['textfield']
    passw = request.POST['textfield2']
    obj = Login.objects.filter(username=user, password=passw)
    if obj.exists():
        obj = obj[0]
        request.session['lid'] = obj.id
        # newobj=obj[0]
        if obj.usertype == 'admin':
            return HttpResponse("<script>alert('Logged in successfully'); window.location='/admin_homepage'</script>")
        elif obj.usertype == 'doctor':
            return HttpResponse("<script>alert('Logged in successfully'); window.location='/Doc_home'</script>")
        else:
            return HttpResponse("<script>alert('Username or password is incorrect'); window.location='/'</script>")
    else:
        return HttpResponse("<script>alert('Username or password is incorrect'); window.location='/'</script>")

def forget_password(request):

   return render(request, 'doctor/forget password.html')


def forgetpassword_post(request):
    f = request.POST['textfield']
    l = Login.objects.filter(username=f)
    if l.exists():
        psw=l[0].password
        try:
            gmail = smtplib.SMTP('smtp.gmail.com', 587)

            gmail.ehlo()

            gmail.starttls()

            gmail.login(e_email, emailpassword)

        except Exception as e:
            print("Couldn't setup email!!" + str(e))

        msg = MIMEText("Password Information for Care Plus " +psw)

        msg['Subject'] = 'Care Plus'

        msg['To'] = l[0].username

        msg['From'] = 'careplus@gmail.com '

        try:

            gmail.send_message(msg)

        except Exception as e:

            print("COULDN'T SEND EMAIL", str(e))

        return HttpResponse(
            "<script>alert('Check your mail');window.location='/'</script>")
    else:
        return HttpResponse(
            "<script>alert('Invalid username');window.location='/forget_password'</script>")

def change_password(request):
    return render(request, 'admin/Change Password.html')


def change_password_post(request):
    old_password = request.POST['textfield']
    new_password = request.POST['textfield2']
    confirm_password = request.POST['textfield3']
    cpw = Login.objects.filter(password=old_password, usertype='admin')

    if cpw.exists():
        if new_password == confirm_password:
            cpw.update(password=new_password)
            return HttpResponse("<script>alert('Password changed succesfully'); window.location='/'</script>")
        else:
            return HttpResponse("<script>alert('password doesn't match'); window.location='/'</script>")
    else:
        return HttpResponse("<script>alert('wrong password'); window.location='/'</script>")


def view_users(request):
    obj = User.objects.all()
    return render(request, 'admin/users.html', {"data": obj})


def view_complaints(request):
    obj = Complaint.objects.all()
    return render(request, 'admin/view complaints.html', {"data": obj})


def Approved_Docs(request):
    try:
        s = request.POST['selected_value']
        if s == 'all':
            arpv = Doctor.objects.filter(LOGIN__usertype="doctor")
            return render(request, 'admin/Approved Docs.html', {'data': arpv})
        else:
            aprv = Doctor.objects.filter(status=s)
            return render(request, 'admin/Approved Docs.html', {'data': aprv})
    except Exception as e:
        aprv = Doctor.objects.filter(LOGIN__usertype="doctor")
        return render(request, 'admin/Approved Docs.html', {'data': aprv})


def Approved_Doc(post):
    type = post.POST('selected_value')
    aprv = Doctor.objects.filter(status=type, LOGIN__usertype="doctor")
    return render(request, 'admin/', {'data': aprv})


def Admin_approval(request):
    adm = Doctor.objects.filter(LOGIN__usertype='pending')
    return render(request, 'admin/Admin Doc Approval.html', {'data': adm})


def Approve_Doc(request, id):
    Login.objects.filter(id=id).update(usertype='doctor')

    return HttpResponse('<script>alert("approved successfully");window.location="/Admin_approval"</script>')


def Reject_Doc(request, id):
    Login.objects.filter(id=id).delete()
    return HttpResponse('<script>alert("rejected successfully");window.location="/Admin_approval"</script>')


def Block(request, id):
    Login.objects.filter(id=id).update(usertype='blocked')
    return HttpResponse('<script>alert("Blocked Successfully");window.location="/Approved_Docs"</script>')


def Unblock(request, id):
    Login.objects.filter(id=id).update(usertype='doctor')
    return HttpResponse('<script>alert("unblocked successfully");window.location="/Approved_Docs"</script>')


def blocked(request, id):
    blc = Doctor.objects.filter(id=id).update(usertype='blocked')
    return render(request, 'admin/Blocked.html', {'data': blc})


def block_list(request):
    na = request.POST['']
    blc = Doctor.objects.filter(name_contains=na)
    return render(request, 'admin/Blocked.html', {'data': blc})


def replycomplaint(request, id):
    return render(request, 'admin/Reply to complaint.html', {'id': id})


def send_reply(request, id):
    re = request.POST['textarea']
    d = datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
    Complaint.objects.filter(id=id).update(reply=re, replydate=d)
    return HttpResponse("replied")


def review(request):
    rv = Review.objects.all()
    return render(request, 'admin/review.html', {"data": rv})


def admin_homepage(request):
    return render(request, 'admin/adminindex.html')


def dochomepage(request):
    return render(request, 'doctor/docindex.html')


def docprofile(request):
    pr = Doctor.objects.get(LOGIN=request.session['lid'])
    return render(request, 'doctor/Profile Management.html', {'data': pr})


def docprofile_post(request, id):
    try:
        name = request.POST['Docname']
        image = request.FILES['image']
        date = datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
        fs = FileSystemStorage()
        fs.save(
            r"C:\Users\Mubashir\Desktop\Diabetic-Retinopathy-Prediction\app\myapp\static\doctor_photos"+ date + '.jpg',
            image)
        path = "/static/doctor_photos/" + date + '.jpg'
        email = request.POST['email']
        phone = request.POST['phone']
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']
        qualification = request.POST['qualification']

        Doctor.objects.filter(id=id).update(name=name, image=path, email=email,
                                            phone_no=phone, latitude=latitude, longitude=longitude,
                                            qualification=qualification)

        return HttpResponse(
            "<script>alert('Doctor profile updated sucessfully');window.location='/Doc_Profile'</script>")

    except Exception as e:
        name = request.POST['Docname']
        email = request.POST['email']
        phone = request.POST['phone']
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']
        qualification = request.POST['qualification']

        Doctor.objects.filter(id=id).update(name=name, email=email,
                                            phone_no=phone, latitude=latitude, longitude=longitude,
                                            qualification=qualification)
        return HttpResponse(
            "<script>alert('Doctor profile updated sucessfully');window.location='/Doc_Profile'</script>")


def register_doc(request):
    return render(request, 'doctor/register.html')


def schedule(request):
    yesterday = datetime.datetime.today() - timedelta(days=1)
    schedules = Schedule.objects.filter(DOCTOR__LOGIN=request.session['lid'],
                                        Date__gte=yesterday.strftime("%Y-%m-%d")).order_by('-id')

    return render(request, 'doctor/Schedule.html', {'data': schedules})

def delete_prescriptions(request,id):
    Prescription.objects.filter(id=id).delete()
    return HttpResponse(
        "<script>alert('Deleted sucessfully');window.location='/schedule'</script>")

def delete_prescription(request,id):
    Appointment.objects.filter(id=id).delete()
    return HttpResponse(
        "<script>alert('Deleted sucessfully');window.location='/schedule'</script>")

def schedule_post(request):
    date = request.POST['Date']
    From_Time = request.POST['From_Time']
    To_Time = request.POST['To_Time']
    Token = request.POST['Token']
    Fee = request.POST['Fee']

    scheduleObj = Schedule()
    scheduleObj.DOCTOR = Doctor.objects.get(LOGIN=request.session['lid'])
    scheduleObj.Date = date
    scheduleObj.From_time = From_Time
    scheduleObj.To_Time = To_Time
    scheduleObj.Token = Token
    scheduleObj.fee = Fee

    scheduleObj.save()
    return HttpResponse("<script>alert('added');window.location='/schedule'</script>")

@csrf_exempt
def updateschedule(request):
    res = Schedule.objects.get(id=request.POST['sid'])
    return render(request, 'doctor/updateSchedule.html', {"data": res})


def updateschedule_post(request, id):
    date = request.POST['Date']
    From_Time = request.POST['From_time']
    To_Time = request.POST['To_Time']
    Schedule.objects.filter(id=id).update(Date=date, From_time=From_Time, To_Time=To_Time)

    return HttpResponse("<script>alert('updated sucessfully');window.location='/schedule'</script>")


def deleteschedule(request, id):
    Schedule.objects.filter(id=id).delete()
    return HttpResponse("<script>alert('DELETED sucessfully');window.location='/schedule'</script>")


def paymentHistory(request):
    date = datetime.datetime.now().strftime('%Y-%m-%d')
    payment = Appointment.objects.filter(Schedule__DOCTOR__LOGIN=request.session['lid'], Date__lte=date)
    return render(request, 'doctor/payment_history.html', {'data': payment})


def register_doc_post(request):
    name = request.POST['name']
    image = request.FILES['image']
    date = datetime.datetime.now().strftime("%Y%m%d-%H%M%S%f")
    fs = FileSystemStorage()
    fs.save(r"C:\Users\Mubashir\Desktop\Diabetic-Retinopathy-Prediction\app\myapp\static\assets\doctor_photos" + date + '.jpg',
            image)
    path = "/static/doctor_photos/" + date + '.jpg'
    email = request.POST['email']
    phone = request.POST['phone']
    # latitude = request.POST['latitude']
    # longitude = request.POST['longitude']
    date = datetime.datetime.now().strftime("%Y%m%d-%H%M%S%f")
    qualification = request.FILES['qualification']
    fs.save(r"C:\Users\Mubashir\Desktop\Diabetic-Retinopathy-Prediction\app\myapp\static\doctor_photos" + date + '.pdf',
            qualification)
    qpath = "/static/doctor_photos/" + date + ".pdf"
    password = request.POST['password']
    cpassword = request.POST['cpassword']

    if Login.objects.filter(username = email).exists():
        return HttpResponse("<script>alert('Email already exists');window.location='/'</script>")

    if password == cpassword:

        userObj = Login()
        userObj.username = email
        userObj.password = password
        userObj.usertype = "pending"
        userObj.save()

        docObj = Doctor()
        docObj.LOGIN = userObj
        docObj.name = name
        docObj.image = path
        docObj.email = email
        docObj.phone = phone

        docObj.qualification = qpath
        docObj.password = password
        docObj.save()
        return HttpResponse("<script>alert('Doctor added');window.location='/'</script>")
    else:
        return HttpResponse("<script>alert('Password Mismatch');window.location='/'</script>")


#     Appointments

def appointments(request, id):
    request.session['aid'] = id
    res = Appointment.objects.filter(Schedule_id=id)
    return render(request, 'doctor/viewAppointments.html', {"data": res})

def upload_retinal_doc(request, id):
    return render(request, "doctor/uploadretinalimage.html", {'aid':id})
import os
import datetime
from django.shortcuts import HttpResponse
from django.core.files.storage import FileSystemStorage
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np
from myapp.models import Appointment, PredictionResult  # Ensure correct imports for your models

# Load the trained diabetic retinopathy model
model = load_model("myapp/models/dr-densenet169.h5")

# Class labels for diabetic retinopathy
class_labels = ['No DR', 'Mild', 'Moderate', 'Severe', 'Proliferative DR']

def predict_diabetic_retinopathy(img_path):
    """Predict diabetic retinopathy stage from an image."""
    # Load and preprocess the image
    img = image.load_img(img_path, target_size=(224, 224))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)  # Add batch dimension
    img_array = img_array / 255.0  # Normalize pixel values to [0, 1]
    
    # Make prediction
    predictions = model.predict(img_array)
    predicted_class = np.argmax(predictions)
    confidence = np.max(predictions)
    return class_labels[predicted_class], confidence

def upload_retinal_doc_post(request, id):
    img = request.FILES['retinalimage']
    fname = datetime.datetime.now().strftime("%Y%m%d%H%M%S") + ".png"
    
    # Save the uploaded image
    folder_path = r"C:\Users\Mubashir\Desktop\Diabetic-Retinopathy-Prediction\app\myapp\static\fassets\predictionimages"
    if not os.path.exists(folder_path):
        os.makedirs(folder_path)
    
    file_path = os.path.join(folder_path, fname)
    FileSystemStorage().save(file_path, img)
    path = "/static/fassets/predictionimages/" + fname  # Relative URL for the saved image

    # Predict the stage of diabetic retinopathy
    predicted_stage, confidence = predict_diabetic_retinopathy(file_path)

    # Add prediction to the database
    try:
        res = Appointment.objects.get(id=id)
    except Appointment.DoesNotExist:
        return HttpResponse("<script>alert('Appointment not found.');window.location='/upload_retinal_doc/"+id+"';</script>")
    
    obj = PredictionResult()
    obj.USER = res.USER
    obj.image = path
    obj.result = predicted_stage
    obj.date = datetime.datetime.now().strftime("%Y-%m-%d")
    obj.save()

    # Return response with the result
    print("Result: ", predicted_stage)
    return HttpResponse(
        "<script>alert('Result: "+str(predicted_stage)+" with confidence: "+str(confidence*100)+"%');"
        "window.location='/upload_retinal_doc/"+id+"';</script>"
    )

@csrf_exempt
def appointment_history(request):
    ress = Appointment.objects.filter(Schedule__DOCTOR__LOGIN=request.session['lid'])
    return render(request, 'doctor/appointment history.html', {"data": ress})

@csrf_exempt
def appointment_history_post(request):
    date = request.POST['SDate']
    ress = Appointment.objects.filter(Schedule__DOCTOR__LOGIN=request.session['lid'], Date=date)
    return render(request, 'doctor/appointment history.html', {"data": ress})

@csrf_exempt
def appointment_history_name_filter(request):
    name = request.POST['name']
    ress = Appointment.objects.filter(USER__name__contains=name,Schedule__DOCTOR__LOGIN=request.session['lid'])
    return render(request, 'doctor/appointment history.html', {"data": ress})


# User Review

def Review_View(request):
    reviews = Review.objects.filter(DOCTOR__LOGIN=request.session['lid'])
    return render(request, 'doctor/review.html', {"data": reviews})


# Upload Prescriptions
def uploadPrescription(request, id):
    return render(request, 'doctor/upload_prescription.html', {"id": id})


def uploadPrescription_post(request, id):
    aid = request.session['aid']
    prescription = request.POST['prescription']
    pr = Prescription.objects.filter(APPOINTMENT=id)
    if pr.exists():
        return HttpResponse("<script>alert('already uploaded');window.location='/appointments/" + aid + "'</script>")
    else:
        prescriptionObj = Prescription()
        prescriptionObj.APPOINTMENT_id = id
        prescriptionObj.Prescription = prescription
        prescriptionObj.save()
        return HttpResponse(
            "<script>alert('Prescription uploaded');window.location='/appointments/" + aid + "'</script>")


def deleteprescription(request, id):
    Prescription.objects.get(APPOINTMENT_id=id).delete()
    aid = request.session['aid']
    return HttpResponse("<script>alert('DELETED sucessfully');window.location='/appointments/" + aid + "'</script>")


# =============================================================dr chat==================================================
@csrf_exempt
def chatt(request, u):
    request.session['head'] = "CHAT"
    return render(request, 'doctor/chat.html', {'u': u})


@csrf_exempt
def chatsnd(request, u):
    d = datetime.datetime.now().strftime("%Y-%m-%d")
    # t=datetime.datetime.now().strftime("%H:%M:%S")
    c = request.session['lid']
    m = request.POST['m']
    cc = Doctor.objects.get(LOGIN__id=c)
    uu = User.objects.get(id=u)
    obj = Chat()
    obj.date = d
    obj.Type = 'doctor'
    obj.DOCTOR = cc
    obj.USER = uu
    obj.Message = m
    obj.save()
    r = JsonResponse({"status": "ok"})
    return r
    # else:
    #     return redirect('/')

@csrf_exempt
def chatrply(request, u):
    try:
        c = request.session['lid']
        cc = Doctor.objects.get(LOGIN__id=c)
        res = Chat.objects.filter(DOCTOR=cc, USER=u)
        v = []
        if len(res) > 0:
            for i in res:
                v.append({
                    'type': i.Type,
                    'chat': i.Message,
                    'name': i.USER.name,
                    'upic': i.USER.image,
                    'dtime': i.date,
                    'tname': i.DOCTOR.name,
                })
            # print(v)
            return JsonResponse({"status": "ok", "data": v})
        else:
            return JsonResponse({"status": "error"})
    except Exception as e:
        return JsonResponse({"status": "ok"})


def patient_details(request):
    obj = User.objects.all()
    return render(request, 'doctor/patients_list.html', {"data": obj})

@csrf_exempt
def and_login(request):
    usn = request.POST['usr']
    psd = request.POST['psw']
    obj = Login.objects.filter(username=usn, password=psd, usertype="user")
    if obj.exists():
        ob = User.objects.get(LOGIN=obj[0])
        return JsonResponse({"status": "ok", "lid": obj[0].id, "name": ob.name, "image": ob.image, "email": ob.email})
    return JsonResponse({"status": "no"})

@csrf_exempt
def android_view_doc(request):
    latitude = request.POST.get('latitude', '')
    longitude = request.POST.get('longitude', '')
    gcd_formula = "6371 * acos(least(greatest(cos(radians(%s)) * cos(radians('" + latitude + "')) * cos(radians('" + longitude + "') - radians(%s)) + sin(radians(%s)) * sin(radians('" + latitude + "')), -1), 1))"
    qry = Doctor.objects.filter(LOGIN__usertype="doctor")
    li = []
    for i in qry:
        qs = Doctor.objects.filter(id=i.id).annotate(
            distance=RawSQL(gcd_formula, (i.latitude, i.longitude, i.latitude))).order_by('distance')
        li.append({
            "id": i.id,
            "doctor_distance": qs[0].distance,
            'doc_name': i.name,
            'email': i.email,
            'doc_q': i.qualification,
            'doc_phone': i.phone_no,
            'doc_img': i.image,
            'latitude': i.latitude,
            'longitude': i.longitude,
        })

    #### Distance arranging.........................

    def hospital_nearby_sort(e):
        return e['doctor_distance']

    li.sort(key=hospital_nearby_sort)
    return JsonResponse({"status": "ok", "users": li})

@csrf_exempt
def android_view_schedule(request):
    did = request.POST["did"]
    res = Schedule.objects.filter(DOCTOR=did)
    li = []
    for i in res:
        li.append({
            'id': i.id,
            'doc_date': i.Date,
            'doc_from': i.From_time,
            'doc_to': i.To_Time,
            'token_A': i.Token,
            'amount': i.fee,

        })
    return JsonResponse({"status": "ok", "users": li})

@csrf_exempt
def book_appointment(request):
    lid = request.POST["lid"]
    sid = request.POST["schid"]
    u = User.objects.get(LOGIN=lid)
    if Appointment.objects.filter(Schedule=sid, USER=u.id).exists():
        return JsonResponse({"status": "bkd"})
    app = Appointment.objects.filter(Schedule=sid)
    tt = int(Schedule.objects.get(id=sid).Token)
    if app.exists():
        t = 0

        for i in app:
            if tt <= int(i.token):
                return JsonResponse({"status": "No"})

            if t < int(i.token):
                t = 0
                t += int(i.token)

        Appointment(
            Schedule_id=sid,
            USER=User.objects.get(LOGIN=lid),
            Date=datetime.datetime.now().date(),
            token=t + 1,
            payment_date=datetime.datetime.now().date(),
            payment_status='paid'
        ).save()
        tt -= 1
        Schedule.objects.filter(id=sid).update(Token=tt)
        return JsonResponse({"status": "ok"})
    else:
        Appointment(
            Schedule_id=sid,
            USER=User.objects.get(LOGIN=lid),
            Date=datetime.datetime.now().date(),
            token='1',
            payment_date=datetime.datetime.now().date(),
            payment_status='paid'
        ).save()
        tt -= 1
        Schedule.objects.filter(id=sid).update(Token=tt)
        return JsonResponse({"status": "ok"})

@csrf_exempt
def android_view_appointment(request):
    lid = request.POST['lid']
    data = Appointment.objects.filter(USER__LOGIN=lid)
    ary = []
    for i in data:
        ary.append({
            "doc_name": i.Schedule.DOCTOR.name,
            "id": i.Schedule.DOCTOR.id,
            "doc_phone": i.Schedule.DOCTOR.phone_no,
            "doc_email": i.Schedule.DOCTOR.email,
            "doc_status": i.Schedule.DOCTOR.status,
            "token": i.token,
            "doc_from_time": i.Schedule.From_time,
            "doc_to_time": i.Schedule.To_Time,
            'aid': i.id,
        })
    return JsonResponse({"status": "ok", "users": ary})

@csrf_exempt
def and_send_review(request):
    lid = request.POST['lid']
    did = request.POST['did']
    revie = request.POST['revie']
    rate = request.POST['rate']
    obj = Review()

    obj.USER = User.objects.get(LOGIN=lid)
    obj.DOCTOR_id = did
    obj.review = revie
    obj.rate = int(float(rate))
    obj.Date = datetime.datetime.now().date()
    obj.save()
    return JsonResponse({"status": "ok"})

@csrf_exempt
def android_view_reply(request):
    lid = request.POST['lid']
    data = Complaint.objects.filter(USER__LOGIN=lid)
    ary = []
    for i in data:
        ary.append({
            "id": i.id,
            "complaint": i.complaint,
            "admin_reply": i.reply,

        })
    return JsonResponse({"status": "ok", "users": ary})

@csrf_exempt
def and_send_complaint(request):
    lid = request.POST['lid']

    com = request.POST['compl']
    obj = Complaint()
    obj.USER = User.objects.get(LOGIN=lid)
    obj.complaint = com
    obj.complaint_date = datetime.datetime.now().date()
    obj.reply = 'Pending'
    obj.reply_date = 'Pending'
    obj.save()
    return JsonResponse({"status": "ok"})

@csrf_exempt
def add_chat(request):
    lid = request.POST['lid']
    toid = request.POST['toid']
    message = request.POST['message']
    d = datetime.datetime.now().strftime("%Y-%m-%d")
    t = datetime.datetime.now().strftime("%H:%m:%d")
    expid = Doctor.objects.get(id=toid)
    uid = User.objects.get(LOGIN=lid)
    obj = Chat()
    obj.date = d
    # obj.time=t
    obj.Type = 'user'
    obj.DOCTOR = expid
    obj.USER = uid
    obj.Message = message
    obj.save()
    return JsonResponse({'status': "Inserted"})


@csrf_exempt
def view_chat(request):
    lid = request.POST['lid']
    toid = request.POST['toid']
    print(lid, toid)
    lastid = request.POST['lastid']
    res = Chat.objects.filter(USER__LOGIN=lid, DOCTOR=toid, id__gt=lastid)
    ar = []
    for i in res:
        ar.append({
            "id": i.id,
            "date": i.date,
            "userid": i.USER.id,
            "sid": i.Type,
            "chat": i.Message,
        })
    return JsonResponse({'status': "ok", 'data': ar})

def viewprescription(request, id):
    data = Prescription.objects.filter(APPOINTMENT=id)
    return render(request, 'doctor/viewprescription.html', {"data": data})


def view_prediction_history(request, id):
    obj = PredictionResult.objects.filter(USER=id).order_by('-id')
    return render(request, 'doctor/DRhistory.html', {"data": obj})


def and_viewprescription(request):
    aid = request.POST['aid']
    data = Prescription.objects.get(APPOINTMENT=aid)
    return JsonResponse({"status": "ok", "imag": data.Prescription})

@csrf_exempt
def android_register(request):
    name = request.POST['na']
    email = request.POST['em']
    lat = request.POST['lat']
    long = request.POST['log']

    phone = request.POST['phon']
    p = request.POST['p']
    image = request.FILES['pic']
    d = datetime.datetime.now().strftime('%d%m%Y-%H%M%S')
    FileSystemStorage().save(
        r"C:\Users\Mubashir\Desktop\Diabetic-Retinopathy-Prediction\app\myapp\static\user_photo\\" + d + '.jpg', image)

    if Login.objects.filter(username=email).exists():
        return JsonResponse({"status": "Email Already Exists"})
    else:
     log = Login(
        username=email,
        password=p,
        usertype='user'
     )
     log.save()
     User(
        LOGIN=log,
        name=name,
        phone_no=phone,
        email=email,
        image="/static/user_photo/" + d + '.jpg',
        latitude=lat,
        longitude=long,

     ).save()
     return JsonResponse({"status": "ok"})


#def prediction(request):
    lid = request.POST['lid']
    fname = datetime.datetime.now().strftime("%Y%m%d%H%M%S") + ".png"
    # if os.path.exists(r"C:\Users\ACER\Desktop\All Folders\FINAL YEAR POROJECT\app\myapp\static\\" + fname):
    #     os.remove(r"C:\Users\ACER\Desktop\All Folders\FINAL YEAR POROJECT\app\myapp\static\\" + fname)
    img = request.FILES['pic']
    FileSystemStorage().save(r"C:\Users\Mubashir\Desktop\Diabetic-Retinopathy-Prediction\app\myapp\static\\" + fname, img)
    path = "/static/" + fname
    import numpy as np
    import pandas as pd
    from skimage import io, color, img_as_ubyte
    from skimage.feature import greycomatrix, greycoprops
    from sklearn.ensemble import RandomForestClassifier
    from sklearn.model_selection import train_test_split

    data = pd.read_csv('C:\\Users\\ACER\\Desktop\\All Folders\\FINAL YEAR POROJECT\\app\\myapp\\static\\features.csv')
    X = data.values[1:, 0:5]
    Y = data.values[1:, 5]

    X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2)
    rgbImg = io.imread(
        r"C:\Users\ACER\Desktop\All Folders\FINAL YEAR POROJECT\app\myapp\static\\" + fname)  # images of disease in rgb
    grayImg = img_as_ubyte(color.rgb2gray(rgbImg))  # images of disease gray
    distances = [1, 2, 3]
    angles = [0, np.pi / 4, np.pi / 2, 3 * np.pi / 4]

    glcm = greycomatrix(grayImg, distances=distances, angles=angles, symmetric=True, normed=True)

    properties = ['energy', 'homogeneity', 'dissimilarity', 'correlation', 'contrast']
    feats = np.hstack([greycoprops(glcm, 'energy').ravel() for prop in properties])
    feats1 = np.hstack([greycoprops(glcm, 'homogeneity').ravel() for prop in properties])
    feats2 = np.hstack([greycoprops(glcm, 'dissimilarity').ravel() for prop in properties])
    feats3 = np.hstack([greycoprops(glcm, 'correlation').ravel() for prop in properties])
    feats4 = np.hstack([greycoprops(glcm, 'contrast').ravel() for prop in properties])

    aa = []
    k = np.mean(feats)  # mean value of features
    l = np.mean(feats1)
    m = np.mean(feats2)
    n = np.mean(feats3)
    o = np.mean(feats4)
    aa.append(k)  # append to array aa
    aa.append(l)
    aa.append(m)
    aa.append(n)
    aa.append(o)

    arr = np.array([aa])
    rf = RandomForestClassifier(n_estimators=100)
    rf.fit(X_train, Y_train)
    cls = rf.predict(arr)

    # Add to Database

    obj = PredictionResult()
    obj.USER = User.objects.get(LOGIN=lid)
    obj.image = path
    obj.result = cls[0]
    obj.date = datetime.datetime.now().strftime("%Y-%m-%d")
    obj.save()

    return JsonResponse({"status": "ok", "result": cls[0]})

@csrf_exempt
def android_view_prescription(request):
    aid = request.POST['aid']

    obj = Prescription.objects.filter(APPOINTMENT=aid)
    if obj.exists():
        p = obj[0].Prescription
    else:
        p = "Not Available Yet!"
    return JsonResponse({"status": "ok", "prescription": p})

def upload_retinal(request):

    return None


@csrf_exempt
def android_view_prediction(request):
    lid = request.POST['lid']
    obj = PredictionResult.objects.filter(USER__LOGIN_id=lid)
    data=[]
    for i in obj:
        data.append({
            "date":i.date, "pred":i.result, "image":i.image, "id":i.id
        })
    return JsonResponse({"status": "ok", "users": data})


import os
import cv2
import numpy as np
from django.shortcuts import render
from django.core.files.storage import FileSystemStorage
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import tensorflow as tf

# Load the trained model
model = load_model("myapp/models/dr-densenet169.h5")

# Class labels
class_labels = ['No DR', 'Mild', 'Moderate', 'Severe', 'Proliferative DR']

def is_retinal_image(img_path):
    img = cv2.imread(img_path)
    if img is None:
        return False

    # Convert to grayscale and apply adaptive threshold
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    _, binary = cv2.threshold(blurred, 50, 255, cv2.THRESH_BINARY)

    # Detect contours
    contours, _ = cv2.findContours(binary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    if not contours:
        return False

    # Find largest contour
    largest_contour = max(contours, key=cv2.contourArea)
    perimeter = cv2.arcLength(largest_contour, True)
    area = cv2.contourArea(largest_contour)

    # Circularity check
    circularity = 4 * np.pi * area / (perimeter ** 2) if perimeter > 0 else 0

    # Check for expected retinal image color
    avg_color = np.mean(img, axis=(0, 1))  # Get average BGR color
    is_fundus_color = avg_color[2] > avg_color[1] > avg_color[0]  # R > G > B in fundus images

    return circularity > 0.6 and is_fundus_color

def predict_diabetic_retinopathy(img_path):
    """Predict the stage of diabetic retinopathy from an image."""
    # Check if the image is a retinal image
    if not is_retinal_image(img_path):
        return "Invalid Input: Please upload a retinal/fundus image.", 0.0

    # Load and preprocess the image
    img = image.load_img(img_path, target_size=(224, 224))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)  # Add batch dimension
    img_array = tf.keras.applications.densenet.preprocess_input(img_array)

    # Make prediction
    predictions = model.predict(img_array)
    predicted_class = np.argmax(predictions)
    confidence = np.max(predictions)

    return class_labels[predicted_class], confidence

def upload_image(request):
    predicted_stage = None
    confidence = None
    image_url = None
    error_message = None

    if request.method == 'POST' and request.FILES['image']:
        uploaded_image = request.FILES['image']

        # Specify the folder where the image should be saved (e.g., 'uploads' folder inside 'media')
        folder_path = os.path.join('myapp', 'static', 'assets', 'predictionimages')

        # Make sure the folder exists
        if not os.path.exists(folder_path):
            os.makedirs(folder_path)

        # Save the uploaded image in the specified folder
        fs = FileSystemStorage(location=folder_path)
        filename = fs.save(uploaded_image.name, uploaded_image)
        uploaded_image_path = fs.url(filename)

        # Predict the DR stage
        img_path = os.path.join(folder_path, filename)  # Path to the uploaded image
        predicted_stage, confidence = predict_diabetic_retinopathy(img_path)

        # If the prediction is an error message, set the error_message
        if isinstance(predicted_stage, str) and "Invalid Input" in predicted_stage:
            error_message = predicted_stage
            predicted_stage = None
            confidence = None

        image_url = uploaded_image_path  # URL of the uploaded image

    return render(request, 'upload_and_predict.html', {
        'predicted_stage': predicted_stage,
        'confidence': confidence,
        'image_url': image_url,
        'error_message': error_message
    })