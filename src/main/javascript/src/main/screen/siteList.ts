function getSiteList(): HTMLElement {
  return document.getElementById('siteList')!;
}

function getSite(identifier: string) {
  const target = document.getElementById(identifier)!;
  const parent = target.parentElement;

  const prevParentElement = parent ? <HTMLElement>parent.previousElementSibling : null;
  const nextParentElement = parent ? <HTMLElement>parent.nextElementSibling : null;

  const prevElement = prevParentElement ? prevParentElement.firstElementChild : null;
  const nextElement = nextParentElement ? nextParentElement.firstElementChild : null;

  return { target: target, prev: prevElement, next: nextElement };
}

export { getSite, getSiteList };
