import { IElementEvent } from './elementEvent';
import NavLink from '../const/navLink';

export default class GridLayoutChgEvent implements IElementEvent {
  private static readonly GRID_ELEMENT_ID = 'gridLayout';

  execute(): void {
    const navLinks = Array.from(document.getElementsByClassName(NavLink.CLASS_NAME));
    const activeGridNavLink = navLinks.find(
      (element) =>
        element.classList.contains(NavLink.ACTIVE_CLASS_NAME) &&
        (element as HTMLElement).dataset.layout === GridLayoutChgEvent.GRID_ELEMENT_ID,
    );

    if (activeGridNavLink) {
      return;
    }

    this.removeActivate(navLinks);

    this.activate(navLinks);

    const contentLayouts = Array.from(document.getElementsByClassName('contentLayout'));
    contentLayouts.forEach((contentLayout) => {
      if (contentLayout.id === GridLayoutChgEvent.GRID_ELEMENT_ID) {
        contentLayout.classList.remove('hiddenContent');
      } else {
        contentLayout.classList.add('hiddenContent');
      }
    });
  }

  /**
   * 他nav-linkのactivateを削除する
   *
   * @private
   * @param {Element[]} navLinks nav-linkクラス持ちのelement郡
   * @memberof GridLayoutChgEvent
   */
  private removeActivate(navLinks: Element[]): void {
    navLinks.forEach((element) => {
      element.classList.remove(NavLink.ACTIVE_CLASS_NAME);
    });
  }

  private activate(navLinks: Element[]) {
    const gridNavLink = navLinks.find((element) => {
      return (element as HTMLElement).dataset.layout === GridLayoutChgEvent.GRID_ELEMENT_ID;
    });
    gridNavLink!.classList.add(NavLink.ACTIVE_CLASS_NAME);
  }
}
