import { AxiosResponse } from 'axios';
import GaleWingApi from './galeWingApi';
import { EventEmitter } from 'events';

export default class SettingApi {
  private settingsJson: [any];
  private response: Promise<AxiosResponse<any, any>>;

  private static instance: SettingApi;

  constructor() {
    this.response = null;
  }

  private initSettingData() {
    this.response = GaleWingApi.getInstance().settingJson();
    this.response.then((res) => {
      this.settingsJson = res.data;
      this.response = null;
    });
    return this.response;
  }

  async init() {
    if (this.response) {
      // データ取得中
      await this.response;
      return SettingApi.instance;
    } else if (SettingApi.instance) {
      // 初期化完了済み
      return SettingApi.instance;
    } else {
      // 初期化
      await this.initSettingData();
      return SettingApi.instance;
    }
  }

  get(key: string) {
    if (!this.response) {
      return this.settingsJson
        .filter((setting) => {
          return setting.id === key;
        })
        .slice(0, 1)[0].setting;
    } else {
      throw new Error('wait setting response');
    }
  }

  outputLog() {
    console.log(this.settingsJson);
  }
}
