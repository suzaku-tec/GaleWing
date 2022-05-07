import axios, { AxiosResponse } from 'axios';

export default class GwYoutubeApi {
  private readonly baseUrl = 'https://www.youtube.com/channel/';

  private static singleton: GwYoutubeApi;

  private constructor() {}

  static getInstance(): GwYoutubeApi {
    if (!this.singleton) {
      this.singleton = new GwYoutubeApi();
    }

    return this.singleton;
  }

  test(): Promise<AxiosResponse<any>> {
    return axios.get('http://localhost:8080/youtube/videos');
  }

  channelHome(channelId: string) {
    this.ajaxGet(this.baseUrl + channelId);
  }

  channelVideos(channelId: string) {
    var response = this.ajaxGet(this.baseUrl + channelId + '/videos');
    response.then((res) => console.log(res));
  }

  channelPlaylists(channelId: string) {
    this.ajaxGet(this.baseUrl + channelId + '/playlists');
  }

  channelCommunity(channelId: string) {
    this.ajaxGet(this.baseUrl + channelId + '/community');
  }

  channelChannels(channelId: string) {
    this.ajaxGet(this.baseUrl + channelId + '/channels');
  }

  channelAbout(channelId: string) {
    this.ajaxGet(this.baseUrl + channelId + '/channels');
  }

  private ajaxGet(url: string): Promise<AxiosResponse<any>> {
    return axios.get(url);
  }
}

type YoutubeType = 'home' | 'videos' | 'playlists' | 'community' | 'channels' | 'about';
