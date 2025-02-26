require('dotenv').config();


const clientId = "f5fb35eb96cc4160bf54530813485dc4";
const redirectUri = "http://aidanm85.github.io/translator"; // GitHub Pages URL
const authEndpoint = "https://accounts.spotify.com/authorize";
const scopes = ["user-read-currently-playing"];


document.getElementById("login").addEventListener("click", () => {
    console.log("login button clicked!");
    const authUrl = `${authEndpoint}?client_id=${clientId}&response_type=token&redirect_uri=${encodeURIComponent(redirectUri)}&scope=${scopes.join("%20")}`;
    window.location.href = authUrl;
});

function getAccessToken() {
    const hash = window.location.hash.substring(1).split("&").reduce((acc, item) => {
        let parts = item.split("=");
        acc[parts[0]] = decodeURIComponent(parts[1]);
        return acc;
    }, {});
    return hash.access_token;
}

async function getCurrentlyPlaying() {
    const token = getAccessToken();
    if (!token) return;

    const response = await fetch("https://api.spotify.com/v1/me/player/currently-playing", {
        headers: { Authorization: `Bearer ${token}` }
    });

    if (response.ok) {
        const data = await response.json();
        document.getElementById("track").innerText = `Now Playing: ${data.item.name} by ${data.item.artists.map(artist => artist.name).join(", ")}`;
    } else {
        document.getElementById("track").innerText = "No track is currently playing.";
    }
}

// Run on page load
window.onload = getCurrentlyPlaying;
