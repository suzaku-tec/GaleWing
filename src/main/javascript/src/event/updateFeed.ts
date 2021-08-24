import axios from 'axios';
import { IElementEvent } from './elementEvent';

export default class UpdateFeed implements IElementEvent {
  elementId: string;

  constructor(elementId: string) {
    this.elementId = elementId;
  }

  execute() {
    var uri = new URL(window.location.href);

    var uuid = (<HTMLInputElement>document.getElementById(this.elementId)).value;

    console.log('execute', uri);
    axios
      .post(uri.origin + '/feed/update', {
        uuid: uuid,
      })
      .then((response) => {})
      .catch((error) => {
        console.log(error);
      });
  }
}
