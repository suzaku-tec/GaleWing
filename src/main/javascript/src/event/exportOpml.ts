import { IElementEvent } from './elementEvent';
import axios from 'axios';

export default class ExportOpml implements IElementEvent {
  constructor() {}

  execute() {
    var uri = new URL(window.location.href);
    window.location.href = uri.origin + '/opml/export';
  }
}
