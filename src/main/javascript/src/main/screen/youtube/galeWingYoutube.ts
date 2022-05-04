declare class YT {
  static Player: any;
  static PlayerState: any;
}

interface Window {
  onYouTubeIframeAPIReady(): void;
}

window.onload = () => {
  // 2. This code loads the IFrame Player API code asynchronously.
  var tag = document.createElement('script');

  tag.src = 'https://www.youtube.com/iframe_api';
  var firstScriptTag = document.getElementsByTagName('script')[0];

  if (firstScriptTag && firstScriptTag.parentNode) {
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
  }

  // 3. This function creates an <iframe> (and YouTube player)
  //    after the API code downloads.
  var player: any;
  function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
      height: '100%',
      width: '100%',
      videoId: 'M7lc1UVf-VE',
      events: {
        onReady: onPlayerReady,
        onStateChange: onPlayerStateChange,
      },
    });
  }

  // 4. The API will call this function when the video player is ready.
  function onPlayerReady(event: { target: { playVideo: () => void } }) {
    event.target.playVideo();
  }

  // 5. The API calls this function when the player's state changes.
  //    The function indicates that when playing a video (state=1),
  //    the player should play for six seconds and then stop.
  var done = false;
  function onPlayerStateChange(event: { data: any }) {
    if (event.data == YT.PlayerState.PLAYING && !done) {
      setTimeout(stopVideo, 6000);
      done = true;
    }
  }
  function stopVideo() {
    player.stopVideo();
  }

  window.onYouTubeIframeAPIReady = onYouTubeIframeAPIReady;
};
