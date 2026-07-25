import { IElementEvent } from '../elementEvent';

export default class TitleListEvent implements IElementEvent {
  execute(): void {
    const rssLinks = Array.from(document.getElementsByClassName('rss-link'));

    const mdTitleList = rssLinks
      .map((el) => {
        return <HTMLElement>el;
      })
      .map((el) => {
        return el.innerText;
      })
      .map((str) => {
        return '- ' + str;
      })
      .join('\r\n');

    navigator.clipboard.writeText(mdTitleList);
  }
}
