function getSiteList(): HTMLElement {
  return document.getElementById('siteList')!;
}

function getSite(identifier: string) {
  var target = document.getElementById(identifier)!;
  var parent = target.parentElement;

  var prevParentElement = parent ? <HTMLElement>parent.previousElementSibling : null;
  var nextParentElement = parent ? <HTMLElement>parent.nextElementSibling : null;

  var prevElement = prevParentElement ? prevParentElement.firstElementChild : null;
  var nextElement = nextParentElement ? nextParentElement.firstElementChild : null;

  return { target: target, prev: prevElement, next: nextElement };
}

export { getSite, getSiteList };
