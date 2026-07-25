import GaleWingApi from "../api/galeWingApi";
import { IElementEvent } from "./elementEvent";
import UpdateMessageEvent from "./modal/updateMessageEvent";

export default class UpdateFeed implements IElementEvent {
  execute(param?: any): void {
    const ume = new UpdateMessageEvent();
    ume.execute();

    GaleWingApi.getInstance().podcastSync().catch((error: any) => {
      alert('Failed to sync podcast feeds.');
    }).finally(() => {
      ume.close();
    });
  }
}
