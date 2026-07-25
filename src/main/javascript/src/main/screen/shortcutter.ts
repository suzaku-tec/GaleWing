export class Shortcutter {

  init() {

    const elementList = document.querySelectorAll<HTMLElement>('[data-shortcut-key]');

    elementList.forEach((shortcut) => {
      const ev = shortcut.dataset.shortcutEvent;
    });

    document.addEventListener('keydown', (event) => {

      Array.from(elementList)
      .filter(() => isPressShortcutMetaKey(event))
      .filter((shortcut) => {

        const keys = shortcut.dataset.shortcutKey?.split('+');

        if(!event.ctrlKey && keys?.includes('ctrl')) {
          return false;
        }

        if(!event.shiftKey && keys?.includes('shift')) {
          return false;
        }

        if(!event.altKey && keys?.includes('alt')) {
          return false;
        }

        return keys?.includes(event.key);
      }).forEach((shortcut) => {
        const evName = shortcut.dataset.shortcutEvent;
        if(!evName) {
          // イベント指定がない場合は、デフォルトでclickイベントを発火させる
          shortcut.click();
        } else {
          const e = new Event(evName);
          shortcut.dispatchEvent(e);
        }
      });
    });

    function isPressShortcutMetaKey(keyEvent: KeyboardEvent): boolean {
      return keyEvent.altKey || keyEvent.ctrlKey || keyEvent.shiftKey;
    }
  }

}

document.addEventListener('DOMContentLoaded', () => {
  new Shortcutter().init();
});

