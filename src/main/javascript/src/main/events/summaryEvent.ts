import { IElementEvent } from "./elementEvent";
import GaleWingApi from "../api/galeWingApi";

export default class SummaryEvent implements IElementEvent {
  constructor() {
  }

  execute(link: string): void {

    const api = GaleWingApi.getInstance();
    api.summaryAdd(link);
  }

}
