from django.core.management.base import BaseCommand, CommandError
from django.apps import apps as aperol
import datetime

DUE_DAYS = -14 # erase after 2 weeks

class Command(BaseCommand):
    help = "Type the help text here"

    def handle(self, *args, **options):
        interactions = aperol.get_model('core', 'Interaction')

        now = datetime.datetime.now()
        delta = datetime.timedelta(days=DUE_DAYS)
        due_date = now+delta

        query = interactions.objects.filter(date__lte=due_date).delete()
