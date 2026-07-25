import { IElementEvent } from "./elementEvent";
import GaleWingApi from "../api/galeWingApi";

export default class InformationGatheringEvent implements IElementEvent {
  constructor() {
  }

  execute(link: string): void {
    const api = GaleWingApi.getInstance();
    api.informationGatheringAdd(link);
  }

}
