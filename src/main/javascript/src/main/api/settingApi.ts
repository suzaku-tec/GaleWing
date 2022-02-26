import { AxiosResponse } from 'axios';
import GaleWingApi from './galeWingApi';
import { EventEmitter } from 'events';

export default class SettingApi {
  private settingsJson: [any];
  private response: Promise<AxiosResponse<any, any>>;

  constructor() {
    this.response = GaleWingApi.getInstance().settingJson();
    this.response.then((res) => {
      this.settingsJson = res.data;
      this.response = null;
    });
  }

  get(key: string) {
    if (!this.response) {
      return this.settingsJson
        .filter((setting) => {
          return setting.id === key;
        })
        .slice(0, 1)[0];
    } else {
      throw new Error('wait setting response');
    }
  }
}
