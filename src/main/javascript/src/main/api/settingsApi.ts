import { AxiosResponse } from 'axios';
import GaleWingApi from './galeWingApi';

export default class SettingsApi {
  private settingResPromise: Promise<AxiosResponse<any>>;

  constructor() {
    var api = GaleWingApi.getInstance();
    this.settingResPromise = api.settingsJson(window.location.href);
  }

  async get(id: string) {
    var setting = await this.settingResPromise;
    return setting.data[id];
  }
}