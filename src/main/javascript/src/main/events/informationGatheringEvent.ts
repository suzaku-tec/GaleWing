import { IElementEvent } from "./elementEvent";
import GaleWingApi from "../api/galeWingApi";

export default class InformationGatheringEvent implements IElementEvent {
  constructor() {
  }

  execute(link: string): void {
    var api = GaleWingApi.getInstance();
    api.informationGatheringAdd(link);
  }

}
