import os
import base64
import random
import string
import requests
from flask import Flask, request, redirect, session, render_template
from flask import make_response

app = Flask(__name__)

# Your Spotify API credentials
client_id = 'f5fb35eb96cc4160bf54530813485dc4'  # Your clientId
client_secret = 'c2e862490df0433f9c00e6e3e281fd17'  # Your client secret
redirect_uri = 'http://aidanm85.github.io/callback'  # Your redirect uri
state_key = 'spotify_auth_state'

app.secret_key = os.urandom(24)  # Set a random secret key for session management
app.run(port=8888)

def generate_random_string(length):
    """Generate a random string for the state parameter."""
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))


@app.route('/login')
def login():
    """Redirect the user to Spotify's authorization page."""
    state = generate_random_string(16)
    session["state_key"] = state

    scope = 'user-read-currently-playing'
    auth_url = 'https://accounts.spotify.com/authorize?' + \
               f'response_type=code&client_id={client_id}&scope={scope}&' + \
               f'redirect_uri={redirect_uri}&state={state}'

    return redirect(auth_url)


@app.route('/callback')
def callback():
    """Handle the callback and request the access token."""
    code = request.args.get('code')
    state = request.args.get('state')
    stored_state = session.get(state_key)

    if not state or state != stored_state:
        return redirect('/#' + 'error=state_mismatch')

    session.pop(state_key, None)  # Clear the session state

    token_url = 'https://accounts.spotify.com/api/token'
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Basic ' + base64.b64encode(f'{client_id}:{client_secret}'.encode()).decode('utf-8')
    }

    data = {
        'code': code,
        'redirect_uri': redirect_uri,
        'grant_type': 'authorization_code'
    }

    response = requests.post(token_url, headers=headers, data=data)
    if response.status_code == 200:
        tokens = response.json()
        access_token = tokens['access_token']
        refresh_token = tokens['refresh_token']
        session['access_token'] = access_token  # Store the access token in session
        return redirect('/translator')
    else:
        return redirect('/#' + 'error=invalid_token')


@app.route('/translator')
def translator():
    """Check if the user is authenticated and make requests to Spotify API."""

    access_token = session.get('access_token')

    if not access_token:
        return redirect('/login')  # Redirect to login if not authenticated

    # Request URL to get the currently playing track
    url = "https://api.spotify.com/v1/me/player/currently-playing"
    headers = {
        "Authorization": f"Bearer {access_token}"
    }

    track_response = requests.get(url, headers=headers)
    if track_response.status_code == 200:
        track_info = track_response.json()
        track_name = track_info["item"]["name"]
    else:
        track_name = "No track is currently playing."

    # Placeholder for song lyrics (You can add your API or logic here)
    original_lyrics = "Original lyrics placeholder"
    translated_lyrics = "Translated lyrics placeholder"

    return render_template("translator.html", original_lyrics=original_lyrics, translated_lyrics=translated_lyrics, track_name=track_name)



