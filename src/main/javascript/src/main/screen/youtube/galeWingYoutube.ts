import GwYoutubeApi from '../../api/gwYoutubeApi';

// import Swiper styles
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';

// コアバージョンに加えて、ナビゲーションとページネーションを追加する
import Swiper, { Navigation, Pagination, SwiperOptions } from 'swiper';

// モジュールを使用可能に
Swiper.use([Navigation, Pagination]);

declare class YT {
  static Player: any;
  static PlayerState: any;
}

let channelId = '';

declare global {
  interface Window {
    onYouTubeIframeAPIReady(): void;
  }
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

function createYoutubeList(element: { title: string; imgUrl: string; videoId: string }) {
  var div = document.createElement('div');
  div.className = 'swiper-slide';
  var img = document.createElement('img');
  img.src = element.imgUrl;
  var title = document.createElement('p');
  title.innerText = element.title;
  div.appendChild(img);
  div.append(title);
  div.dataset.videoId = element.videoId;
  div.addEventListener('click', () => {
    console.log(element.videoId);
    player.stopVideo();
    player.cueVideoById({ videoId: element.videoId });
  });

  return div;
}

window.onload = () => {
  // 2. This code loads the IFrame Player API code asynchronously.
  var tag = document.createElement('script');

  tag.src = 'https://www.youtube.com/iframe_api';
  var firstScriptTag = document.getElementsByTagName('script')[0];

  if (firstScriptTag && firstScriptTag.parentNode) {
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
  }

  // var testBtn = document.getElementById('test');
  // testBtn?.addEventListener('click', () => {
  //   let gwYoutubeApi = GwYoutubeApi.getInstance();
  //   gwYoutubeApi.test().then((res) => {
  //     var data = res.data;

  //     var swiperWrapper = document.getElementsByClassName('swiper-wrapper')[0];

  //     data.forEach((element: { title: string; imgUrl: string; videoId: string }) => {
  //       var div = createYoutubeList(element);
  //       swiperWrapper.appendChild(div);
  //     });
  //   });
  // });

  GwYoutubeApi.getInstance()
    .test()
    .then((res) => {
      var data = res.data;

      var swiperWrapper = document.getElementsByClassName('swiper-wrapper')[0];

      data.forEach((element: { title: string; imgUrl: string; videoId: string }) => {
        var div = createYoutubeList(element);
        swiperWrapper.appendChild(div);
      });
    });

  window.onYouTubeIframeAPIReady = onYouTubeIframeAPIReady;

  const swiperParams: SwiperOptions = {
    direction: 'horizontal',
    slidesPerView: 3,
    spaceBetween: 50,
    loop: false,
    autoplay: true,
    pagination: {
      el: '.swiper-pagination',
    },
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
    scrollbar: {
      el: '.swiper-scrollbar',
    },
  };

  const swiper = new Swiper('.swiper', swiperParams);
};
