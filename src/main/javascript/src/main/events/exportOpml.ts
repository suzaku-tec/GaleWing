import { IElementEvent } from './elementEvent';

export default class ExportOpml implements IElementEvent {
  constructor() {}

  execute() {
    const uri = new URL(window.location.href);
    window.location.href = uri.origin + '/opml/export';
  }
}
