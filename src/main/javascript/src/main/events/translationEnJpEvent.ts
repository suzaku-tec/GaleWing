import GaleWingApi from '../api/galeWingApi';
import { IElementEvent } from './elementEvent';

export default class TranslationEnJp implements IElementEvent {
  execute(): void {
    // HTMLCollectionOfオブジェクトを取得する
    const elements = document.getElementsByClassName('rss-link');

    // 配列に変換する
    const arrayElements = Array.from(elements) as HTMLElement[];

    const resolve = Promise.resolve();

    arrayElements.forEach((element) => {
      if (element.dataset.translationJpTxt == element.innerText) {
        // 翻訳済み
        return;
      }
      resolve.then(() => {
        return this.createTranslationEnJpPromise(element);
      });
    });
  }

  createTranslationEnJpPromise(element: HTMLElement): Promise<any> {
    return new Promise((resolve, reject) => {
      const dataOriginTxt = element.dataset.originTxt;
      if (!dataOriginTxt) {
        resolve('');
        return;
      }
      GaleWingApi.getInstance()
        .translationEnJp(dataOriginTxt)
        .then((res) => {
          element.innerText = res.data;
          element.dataset.translationJpTxt = res.data;
          resolve('');
        })
        .catch(() => {
          reject();
        });
    });
  }
}
