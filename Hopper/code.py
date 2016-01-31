import os
import webapp2
import logging
import json

from google.appengine.ext.webapp import template
from google.appengine.api import users
from google.appengine.ext import ndb
from google.appengine.ext import webapp
from google.appengine.api import mail


##############################################################################
def render_template(handler, templatename, templatevalues):
	path = os.path.join(os.path.dirname(__file__), 'templates/' + templatename)
	html = template.render(path, templatevalues)
	handler.response.out.write(html)
	
###############################################################################
# We'll use this convenience function to retrieve the current user's email.
def get_user_email():
  result = None
  user = users.get_current_user()
  if user:
    result = user.email()
  return result

###############################################################################
class MainPageHandler(webapp2.RequestHandler):
  def get(self):
    email = get_user_email()

    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/')
    }
    render_template(self, 'index.html', page_params)
###############################################################################
class BeerList(ndb.Model):
  barname = ndb.StringProperty()
  beers = ndb.StringProperty(repeated=True)
  specials = ndb.StringProperty(repeated=True)
  voted = ndb.BooleanProperty()
  
  def add_vote(self, user):
    ImageVote.get_or_insert(user, parent=self.key)
    
  def remove_vote(self, user):
    image_vote = ImageVote.get_by_id(user, parent=self.key)
    if image_vote:
      image_vote.key.delete()
    
  def count_votes(self):
    q = ImageVote.query(ancestor=self.key)
    return q.count()
    
  def is_voted(self, user):
    result = False
    if ImageVote.get_by_id(user, parent=self.key):
      result = True
    return result
    
  def create_comment(self, user, text):
    comment = Comment(parent=self.key)
    comment.user = user
    comment.text = text
    comment.put()
    return comment
    
  def get_comments(self):
    result = list()
    q = Comment.query(ancestor=self.key)
    q = q.order(-Comment.time_created)
    for comment in q.fetch(1000):
      result.append(comment)
    return result
    
  def all_bars(self):
    return BeerList.query().fetch()
    
###############################################################################
class ImageVote(ndb.Model):
  pass

###############################################################################


class BarInfoHandler(webapp2.RequestHandler):
  def post(self):
      text = self.request.get('name')
      email = get_user_email()
      beerlist = BeerList.get_by_id(text)
      
      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
        'beerlist': beerlist
      }
      
      path = os.path.join(os.path.dirname(__file__), 'templates/barinfo.html')
      return render_template(self, 'barinfo.html', page_params)
      
  def get(self):
      email = get_user_email()
      id = self.request.get('name')
      fullname = self.request.get('fullname')
      beerlist = BeerList.get_by_id(id)
      beerlist.countvotes = beerlist.count_votes()
      if(email):
        beerlist.voted = beerlist.is_voted(email)
      beerlist.comments = beerlist.get_comments()
      beerlist.comment_count = len(beerlist.comments)

      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
        'beerlist': beerlist,
        'barid': id
      }
      return render_template(self, 'barinfo.html', page_params)

      
###############################################################################
class EditBeerHandler(webapp2.RequestHandler):
  def post(self):
      text = self.request.get('name')
      email = get_user_email()
      beer = self.request.get('newbeer')
      beerlist = BeerList.get_by_id(text)
      
      beerlist.beers.append(beer)
      #newaddition = BeerList(barname = text, beers = beer)
      
      
      
      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
        'beerlist': beerlist,
        'barname': barname,
        'barid': text
      }
      
      path = os.path.join(os.path.dirname(__file__), 'templates/editbeer.html')
      return render_template(self, 'editbeer.html', page_params)
      
  def get(self):
      text = self.request.get('name')
      email = get_user_email()
      beerlist = BeerList.get_by_id(text)
      #newaddition = BeerList(barname = text, beers = beer)

      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
        'beerlist': beerlist,
        'barid': text
      }
      return render_template(self, 'editbeer.html', page_params)
      
###############################################################################
class EditSubmitHandler(webapp2.RequestHandler):
  def post(self):
    name = self.request.get('name')
    beer = self.request.get('newbeer')
    beerlist = BeerList.get_by_id(name)
    beerlist.beers.append(beer)
    beerlist.put()
    email = get_user_email()
    logging.info("name: " + name)
    logging.info("beer: " + beer)
    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/'),
      'barid': name
    }
    
    return render_template(self, 'editreceived.html', page_params)
    
###############################################################################
class EditSpecialsHandler(webapp2.RequestHandler):
  def get(self):
      text = self.request.get('name')
      email = get_user_email()
      beerlist = BeerList.get_by_id(text)

      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
        'beerlist': beerlist,
        'barid': text
      }
      return render_template(self, 'editspecials.html', page_params)
    
################################################################################
class AboutUsHandler(webapp2.RequestHandler):
  def get(self):
      text = self.request.get('name')
      email = get_user_email()

      
      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
      }
    
      
      path = os.path.join(os.path.dirname(__file__), 'templates/about.html')
      return render_template(self, 'about.html', page_params)
################################################################################

class AllBarsHandler(webapp2.RequestHandler):
  def get(self):
      email = get_user_email()
      id = (self.request.url).rsplit('/',1)
      logging.info('URL: ' + self.request.url)
      logging.info('QUERY: ' + self.request.query_string)
      #logging.info('ID: ' + id)
      beerlist = BeerList.get_by_id(id[1])
      page_params = {
        'user_email': email,
        'login_url': users.create_login_url(),
        'logout_url': users.create_logout_url('/'), 
        'beerlist': beerlist
      }
      return render_template(self, 'barinfo.html', page_params)
    
###############################################################################
class ContactPageHandler(webapp2.RequestHandler):
  def get(self):
    email = get_user_email()
      
    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/')
    }
      
    path = os.path.join(os.path.dirname(__file__), 'templates/contact.html')
    return render_template(self, 'contact.html', page_params)
###############################################################################
class SubmitContactPageHandler(webapp2.RequestHandler):
  def post(self):
    name = self.request.get('user_name')
    message = self.request.get('user_message')
    
    email = get_user_email()
      
    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/')
    }
    
    from_address = 'contact@hopper-1091.appspotmail.com'
    subject = 'Contact from ' + name
    body = 'Message from ' + email + ':\n\n' + message
    mail.send_mail(from_address, '1520hopper@gmail.com', subject, body)
   

    path = os.path.join(os.path.dirname(__file__), 'templates/thanks.html')
    return render_template(self, 'thanks.html', page_params)

   
###############################################################################
class DirectionHandler(webapp2.RequestHandler):
  def get(self): 
    start = self.request.get('start')
    end = self.request.get('end') 
    name = self.request.get('name')
    email = get_user_email()
    page_params = {
  	  'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/'),
  	  'start': start,
  	  'end': end,
  	  'name': name
  	  }
    return render_template(self, 'directions.html', page_params)
    
###############################################################################
class LocationHandler(webapp2.RequestHandler):
  def get(self):
  	addr = self.request.get('addr')
  	email = get_user_email()
  	page_params = {
  	  'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/'),
      'address': addr
  	}
  	return render_template(self, 'locations.html', page_params)
  	
###############################################################################
class HereHandler(webapp2.RequestHandler):
  def get(self):
    email = get_user_email()
    if email:
      id = self.request.get('name')
      beerlist = BeerList.get_by_id(id)
      beerlist.add_vote(email)
      beerlist.countvotes = beerlist.count_votes()
      votes = beerlist.countvotes
    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/'),
      'beerlist': beerlist,
      'barid': id
    }
    
    self.response.write(votes)

###############################################################################
class NotHereHandler(webapp2.RequestHandler):
  def get(self):
    email = get_user_email()
    if email:
      image_id = self.request.get('name')
      beerlist = BeerList.get_by_id(image_id)
      beerlist.remove_vote(email)
      beerlist.countvotes = beerlist.count_votes()
      votes = beerlist.countvotes
    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/'),
      'beerlist': beerlist,
      'barid': image_id
    }
    
    self.response.write(votes)
    
###############################################################################
class CommentHandler(webapp2.RequestHandler):
  def post(self):
    email = get_user_email()
    id = self.request.get('id')
    beerlist = BeerList.get_by_id(id)
    text = self.request.get('comment')
    com = beerlist.create_comment(email, text) 
    time = com.time_created  
    self.response.write(time)



###############################################################################
class Comment(ndb.Model):
  user = ndb.StringProperty()
  text = ndb.TextProperty()
  time_created = ndb.DateTimeProperty(auto_now_add=True) 
  
  def allComments(self):
    return Comment.query().fetch()

###############################################################################
class CleanupHandler(webapp2.RequestHandler):
  def get(self):
    com = Comment()
    delKeys = []
    for x in com.allComments():
      delKeys.append(x.key)
    ndb.delete_multi(delKeys)
    
    beerlist = BeerList()
    delKeys = []
    for x in beerlist.all_bars():
      delKeys.append(x.key)
    ndb.delete_multi(delKeys)
    
###############################################################################   
def get_image(image_id):
  return ndb.Key(urlsafe=image_id).get()

      
###############################################################################
class EditSpecialRecHandler(webapp2.RequestHandler):
  def post(self):
    name = self.request.get('name')
    special = self.request.get('newspecial')
    beerlist = BeerList.get_by_id(name)
    beerlist.specials.append(special)
    beerlist.put()
    email = get_user_email()
    page_params = {
      'user_email': email,
      'login_url': users.create_login_url(),
      'logout_url': users.create_logout_url('/'),
      'barid': name
    }
    
    return render_template(self, 'editreceived.html', page_params)
    
 
###############################################################################
mappings = [
  ('/', MainPageHandler),	
  ('/barinfo', BarInfoHandler),
  (r'<:.*>', AllBarsHandler),
  ('/primantibros', BarInfoHandler),
  ('/contact', ContactPageHandler),
  ('/thanks', SubmitContactPageHandler),
  ('/hemingwayscafe', BarInfoHandler),
  ('/holidayinnpittsburghuniversitycenter', BarInfoHandler),
  ('/bridgesrestaurantlounge', BarInfoHandler),
  ('/luccaristorantewinebar', BarInfoHandler),
  ('/peterspub', BarInfoHandler),
  ('/kboxkaraokehouse', BarInfoHandler),
  ('/madmex', BarInfoHandler),
  ('/garagedoorsaloon', BarInfoHandler),
  ('/butterjoint', BarInfoHandler),
  ('/pittsburghcafe', BarInfoHandler),
  ('/genesplace', BarInfoHandler),
  ('/bootleggers', BarInfoHandler),
  ('/logans', BarInfoHandler),
  ('/fuelandfuddle', BarInfoHandler),
  ('/djasher', BarInfoHandler),
  ('/chiefscafe', BarInfoHandler),
  ('/mitchellstavern', BarInfoHandler),
  ('/unclejimmysbar', BarInfoHandler),
  ('/sphinxcafe', BarInfoHandler),
  ('/directions', DirectionHandler),
  ('/location', LocationHandler),
  ('/here', HereHandler),
  ('/nothere',NotHereHandler),
  ('/comment', CommentHandler),
  ('/cleanup', CleanupHandler),
  ('/editbeer', EditBeerHandler),
  ('/editreceived', EditSubmitHandler),
  ('/about', AboutUsHandler),
  ('/editspecials', EditSpecialsHandler),
  ('/editspecialreceived', EditSpecialRecHandler)
]
app = webapp2.WSGIApplication(mappings, debug=True)



######		BEER		
###############################################################################
hemsbeers = BeerList(barname = "Hemingway's Cafe", 
            beers = ["Bell's 2 Hearted", "Southern Tier 2X", "Lagunitas IPA", "Goose Island IPA", "FatHead's Head Hunter", 
              "Blue Moon White IPA", "Ballast Point Grapefruit Sculpin", "East End Big Hop", "Sierra Nevada Hop Hunter",
              "Dogfish Head 60 Min", "Port Brewing Wipeout", "Deschutes Fresh Squeezed", "Great Lakes Sharpshooter"], 
            specials = ["$2.50 draft specials every day","$2 off all drafts 10-midnight"],
            id = "hemingwayscafe").put();
primantibeers = BeerList(barname = "Primanti Brothers", 
                beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
                specials = ["Mondays: Coors Light Drafts", "Tuesdays: I.C. Light Drafts", 
                "Wednesdays: Miller Light Drafts", "Thursday: Yuengling Drafts", 
                "Fridays: Beer of the Month", "Saturdays: Coors Light Drafts", 
                "Sundays: Budweiser and Bud Light Drafts"],
                id = "primantibros").put();
holidayinnbeers = BeerList(barname = "Holiday Inn",
				  beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
				  specials = ["Happy Hour Daily!"],
				  id = "holidayinnpittsburghuniversitycenter").put();
bridgesbeers = BeerList(barname = "Bridges Restaurant & Lounge",
               beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
               specials = ["None"],
               id = "bridgesrestaurantlounge").put();
luccabeers = BeerList(barname = "Lucca Ristorante & Wine Bar",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["Happy Hour 5-7pm daily"],
			 id = "luccaristorantewinebar").put();
petersbeers = BeerList(barname = "Peter's Pub",
			 beers = ["Bud Light", "Blue Moon", "Coors Light", "Franziskaner Weissbier", "Guiness", "Labatte", "Leinenkugels Berry Weiss", "Sam Adams Seasonal", "Stella Artois", "Yuengling"],
			 specials = ["Monday:", "35 cent wings 9-midnight", "$7.50 Buckets of Beer 9-11", "Tuesday:", "$2 Well Drinks", "$2 Coors Light Bottles 10-Midnight", "Wednesday:", "$2 Yuengling bottles 10-midnight"],
			 id = "peterspub").put();
kboxbeers = BeerList(barname = "KBox Karaoke House",
			 beers = ["None on tap"],
			 specials = ["None"],
			 id = "kboxkaraokehouse").put();
madmexbeers = BeerList(barname = "Mad Mex",
			 beers = ["Mad Mex Dos Decadas Anniversary IPA", "Alpine Duet IPA", "Bell's Two Hearted IPA", "Breckenridge Agave Wheat", "Deschutes India Red Ale", "Deschutes Pine Drops IPA", "Dos Equis Amber Lager", "Fat Heads Spooky Tooth IMP Pumpkin Ale", "Great Lakes Lake Erie Monster IPA", "Jack's Hard Cider", "Left Hand Milk Stout", "Southern Tier Pumking", "Terrapin Pumpkinfest", "Yuengling"],
			 specials = ["Happy Hour every day 5-7","Half-Price food late night"],
			 id = "madmex").put();
gdoorbeers = BeerList(barname = "Garage Door Saloon",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["$2 drafts tuesday 7-11"],
			 id = "garagedoorsaloon").put();
butterjointbeers = BeerList(barname = "Butterjoint",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["4:30-6:30 Monday-Friday:","$4 craft beer","$5 house wine","$7 cocktail of the day"],
			 id = "butterjoint").put();
pcafe = BeerList(barname = "AD's",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["Happy Hour daily 5-7pm"],
			 id = "pittsburghcafe").put();
genesbeers = BeerList(barname = "Gene's Place",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = [],
			 id = "genesplace").put();
bootleggersbeers = BeerList(barname = "Bootlegger's",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = [],
			 id = "bootleggers").put();
logansbeers = BeerList(barname = "Logan's",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = [],
			 id = "logans").put();
fuelandfuddlebeers = BeerList(barname = "Fuel and Fuddle",
			 beers = ["Green Flash IPA", "Blue Point Rastafa Rye", "Grist House American Red Ale","Yuengling","Great Lakes Spacewalker","Paulaner Oktoberfest","Vinta Punkin Ale","Shipyard PumpkinHead","North Country Buck Snort Stout"],
			 specials = ["Dollar Well Drinks 9-11"],
			 id = "fuelandfuddle").put();
djasherbeers = BeerList(barname = "DJ Asher",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = [],
			 id = "djasher").put();
chiefsbeers = BeerList(barname = "Chief's Cafe",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["NA"],
			 id = "chiefscafe").put();
mitchellsbeers = BeerList(barname = "Mitchell's Tavern",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["NA"],
			 id = "mitchellstavern").put();
unclejimmysbeers = BeerList(barname = "Uncle Jimmy's Bar",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["NA"],
			 id = "unclejimmysbar").put();
sphinxbeers = BeerList(barname = "Sphinx Cafe",
			 beers = ["Blue Moon", "Yuengling", "Miller Lite", "Bud Light", "Coors Light", "Budweiser"],
			 specials = ["NA"],
			 id = "sphinxcafe").put();
  