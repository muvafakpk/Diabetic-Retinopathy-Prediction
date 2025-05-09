from django.db import models

# Create your models here.
from django.db import models

# Create your models here.

class Login(models.Model):
    username = models.CharField(max_length=300)
    password = models.CharField(max_length=200)
    usertype = models.CharField(max_length=200)

class Doctor(models.Model):

    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)
    image=models.CharField(max_length=150)
    name=models.CharField(max_length=300)
    email=models.CharField(max_length=400)
    phone_no=models.CharField(max_length=1000)
    latitude=models.CharField(max_length=1000,default="")
    longitude=models.CharField(max_length=1000,default="")
    qualification=models.CharField(max_length=500,default="")
    status = models.CharField(max_length=500, default='Unblocked')


class User(models.Model):
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)
    image = models.CharField(max_length=450)
    name = models.CharField(max_length=300)
    email = models.CharField(max_length=400)
    phone_no = models.CharField(max_length=1000)
    latitude = models.CharField(max_length=1000, default="")
    longitude = models.CharField(max_length=1000, default="")

class Complaint(models.Model):
    USER=models.ForeignKey(User,on_delete=models.CASCADE)
    complaint=models.CharField(max_length=500)
    complaint_date=models.CharField(max_length=800)
    reply=models.CharField(max_length=1000)
    reply_date=models.CharField(max_length=800)

class Review(models.Model):
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    DOCTOR=models.ForeignKey(Doctor, on_delete=models.CASCADE)
    review=models.CharField(max_length=500)
    rate = models.CharField(max_length=500)
    Date = models.CharField(max_length=500)

class Schedule(models.Model):
    # USER = models.ForeignKey(User, on_delete=models.CASCADE)
    DOCTOR = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    # Date = models.DateField()
    From_time=models.CharField(max_length=800,default=12)
    To_Time = models.CharField(max_length=800, default=1)
    Token = models.CharField(max_length=800)
    Date=models.CharField(max_length=800)
    fee=models.CharField(max_length=500, default=1)




class Card(models.Model):
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)
    Balance=models.CharField(max_length=1000)

class Chat(models.Model):
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    DOCTOR = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    Message=models.CharField(max_length=500)
    date=models.CharField(max_length=100)
    Type=models.CharField(max_length=1000)

class Appointment(models.Model):
    Schedule = models.ForeignKey(Schedule, on_delete=models.CASCADE,default=1)
    USER = models.ForeignKey(User, on_delete=models.CASCADE,default=1)
    token = models.CharField(max_length=500)
    Date = models.CharField(max_length=500)
    payment_date = models.CharField(max_length=500,default=1)
    payment_status = models.CharField(max_length=500,default=1)


class Prescription(models.Model):
    APPOINTMENT= models.ForeignKey(Appointment, default=1, on_delete=models.CASCADE)
    Prescription=models.CharField(max_length=1000)



class PredictionResult(models.Model):
    image = models.CharField(max_length=100)
    result = models.CharField(max_length=100)
    date = models.CharField(max_length=50)
    USER = models.ForeignKey(User, on_delete=models.CASCADE)









