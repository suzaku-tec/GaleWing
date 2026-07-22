import { IElementEvent } from "./elementEvent";
import GaleWingApi from "../api/galeWingApi";

export default class SummaryEvent implements IElementEvent {
  constructor() {
  }

  execute(link: string): void {

    var api = GaleWingApi.getInstance();
    api.summaryAdd(link);
  }

}
