import { IElementEvent } from '../elementEvent';

export default class TitleListEvent implements IElementEvent {
  execute(): void {
    let rssLinks = Array.from(document.getElementsByClassName('rss-link'));

    let mdTitleList = rssLinks
      .map((el) => {
        return <HTMLElement>el;
      })
      .map((el) => {
        return el.innerText;
      })
      .map((str) => {
        return '- ' + str;
      })
      .join('\n');

    navigator.clipboard.writeText(mdTitleList);
  }
}
