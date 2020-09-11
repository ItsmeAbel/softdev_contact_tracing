from django.urls import path
from .views import UserRecordView

app_name = 'core'
urlpatterns = [
    path('register/', UserRecordView.as_view(), name='register'),
]

