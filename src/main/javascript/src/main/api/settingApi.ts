import { AxiosResponse } from 'axios';
import GaleWingApi from './galeWingApi';

export default class SettingApi {
  private static settingsJson: [any];

  constructor() {
  }
  private async initSettingData() {
    // const response: Promise<AxiosResponse<any, any>> = GaleWingApi.getInstance().settingJson();
    // response.then((res) => {
    //   SettingApi.settingsJson = res.data;
    // });
    // return response;
    const response: AxiosResponse<any, any> = await GaleWingApi.getInstance().settingJson();
    SettingApi.settingsJson = response.data;
  }

  async init() {
    if (SettingApi.settingsJson) {
      return;
    } else {
      // 初期化
      await this.initSettingData();
    }
  }

  get(key: string) {
    if (SettingApi.settingsJson) {
      return SettingApi.settingsJson
        .filter((setting) => {
          return setting.id === key;
        })
        .slice(0, 1)[0].setting;
    } else {
      throw new Error('wait setting response');
    }
  }

  outputLog() {
    console.log(SettingApi.settingsJson);
  }
}
