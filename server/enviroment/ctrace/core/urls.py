from django.urls import path
from .views import UserRecordView, StatusView

app_name = 'core'
urlpatterns = [
    path('register/', UserRecordView.as_view(), name='register'),
    path('status/', StatusView.as_view(), name='status'),
]

