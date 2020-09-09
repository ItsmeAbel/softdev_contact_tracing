from django.db import models

# Create your models here.

from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver
MAX_UUIDS_LEN = 500
class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    uuids = models.TextField(max_length=MAX_UUIDS_LEN, blank=True)
    infected = models.BooleanField(default=False)

    def __str__(self):
        return self.user.__str__()

# create profile when user is created or updated
@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        Profile.objects.create(user=instance)

@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.profile.save()

class Interaction(models.Model):
    user1 = models.ForeignKey(User, related_name="user1", on_delete=models.CASCADE)
    user2 = models.ForeignKey(User, related_name="user2", on_delete=models.CASCADE)
    date = models.DateField()

