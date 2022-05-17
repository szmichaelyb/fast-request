var _t=Object.defineProperty,vt=Object.defineProperties;var gt=Object.getOwnPropertyDescriptors;var Y=Object.getOwnPropertySymbols;var ft=Object.prototype.hasOwnProperty,mt=Object.prototype.propertyIsEnumerable;var q=(i,t,e)=>t in i?_t(i,t,{enumerable:!0,configurable:!0,writable:!0,value:e}):i[t]=e,J=(i,t)=>{for(var e in t||(t={}))ft.call(t,e)&&q(i,e,t[e]);if(Y)for(var e of Y(t))mt.call(t,e)&&q(i,e,t[e]);return i},Q=(i,t)=>vt(i,gt(t));/**
 * @license
 * Copyright 2019 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const K=window.ShadowRoot&&(window.ShadyCSS===void 0||window.ShadyCSS.nativeShadow)&&"adoptedStyleSheets"in Document.prototype&&"replace"in CSSStyleSheet.prototype,W=Symbol(),Z=new Map;class at{constructor(t,e){if(this._$cssResult$=!0,e!==W)throw Error("CSSResult is not constructable. Use `unsafeCSS` or `css` instead.");this.cssText=t}get styleSheet(){let t=Z.get(this.cssText);return K&&t===void 0&&(Z.set(this.cssText,t=new CSSStyleSheet),t.replaceSync(this.cssText)),t}toString(){return this.cssText}}const At=i=>new at(typeof i=="string"?i:i+"",W),yt=(i,...t)=>{const e=i.length===1?i[0]:t.reduce((s,n,r)=>s+(o=>{if(o._$cssResult$===!0)return o.cssText;if(typeof o=="number")return o;throw Error("Value passed to 'css' function must be a 'css' function result: "+o+". Use 'unsafeCSS' to pass non-literal values, but take care to ensure page security.")})(n)+i[r+1],i[0]);return new at(e,W)},St=(i,t)=>{K?i.adoptedStyleSheets=t.map(e=>e instanceof CSSStyleSheet?e:e.styleSheet):t.forEach(e=>{const s=document.createElement("style"),n=window.litNonce;n!==void 0&&s.setAttribute("nonce",n),s.textContent=e.cssText,i.appendChild(s)})},F=K?i=>i:i=>i instanceof CSSStyleSheet?(t=>{let e="";for(const s of t.cssRules)e+=s.cssText;return At(e)})(i):i;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var k;const X=window.trustedTypes,Et=X?X.emptyScript:"",tt=window.reactiveElementPolyfillSupport,V={toAttribute(i,t){switch(t){case Boolean:i=i?Et:null;break;case Object:case Array:i=i==null?i:JSON.stringify(i)}return i},fromAttribute(i,t){let e=i;switch(t){case Boolean:e=i!==null;break;case Number:e=i===null?null:Number(i);break;case Object:case Array:try{e=JSON.parse(i)}catch{e=null}}return e}},ct=(i,t)=>t!==i&&(t==t||i==i),L={attribute:!0,type:String,converter:V,reflect:!1,hasChanged:ct};class A extends HTMLElement{constructor(){super(),this._$Et=new Map,this.isUpdatePending=!1,this.hasUpdated=!1,this._$Ei=null,this.o()}static addInitializer(t){var e;(e=this.l)!==null&&e!==void 0||(this.l=[]),this.l.push(t)}static get observedAttributes(){this.finalize();const t=[];return this.elementProperties.forEach((e,s)=>{const n=this._$Eh(s,e);n!==void 0&&(this._$Eu.set(n,s),t.push(n))}),t}static createProperty(t,e=L){if(e.state&&(e.attribute=!1),this.finalize(),this.elementProperties.set(t,e),!e.noAccessor&&!this.prototype.hasOwnProperty(t)){const s=typeof t=="symbol"?Symbol():"__"+t,n=this.getPropertyDescriptor(t,s,e);n!==void 0&&Object.defineProperty(this.prototype,t,n)}}static getPropertyDescriptor(t,e,s){return{get(){return this[e]},set(n){const r=this[t];this[e]=n,this.requestUpdate(t,r,s)},configurable:!0,enumerable:!0}}static getPropertyOptions(t){return this.elementProperties.get(t)||L}static finalize(){if(this.hasOwnProperty("finalized"))return!1;this.finalized=!0;const t=Object.getPrototypeOf(this);if(t.finalize(),this.elementProperties=new Map(t.elementProperties),this._$Eu=new Map,this.hasOwnProperty("properties")){const e=this.properties,s=[...Object.getOwnPropertyNames(e),...Object.getOwnPropertySymbols(e)];for(const n of s)this.createProperty(n,e[n])}return this.elementStyles=this.finalizeStyles(this.styles),!0}static finalizeStyles(t){const e=[];if(Array.isArray(t)){const s=new Set(t.flat(1/0).reverse());for(const n of s)e.unshift(F(n))}else t!==void 0&&e.push(F(t));return e}static _$Eh(t,e){const s=e.attribute;return s===!1?void 0:typeof s=="string"?s:typeof t=="string"?t.toLowerCase():void 0}o(){var t;this._$Ep=new Promise(e=>this.enableUpdating=e),this._$AL=new Map,this._$Em(),this.requestUpdate(),(t=this.constructor.l)===null||t===void 0||t.forEach(e=>e(this))}addController(t){var e,s;((e=this._$Eg)!==null&&e!==void 0?e:this._$Eg=[]).push(t),this.renderRoot!==void 0&&this.isConnected&&((s=t.hostConnected)===null||s===void 0||s.call(t))}removeController(t){var e;(e=this._$Eg)===null||e===void 0||e.splice(this._$Eg.indexOf(t)>>>0,1)}_$Em(){this.constructor.elementProperties.forEach((t,e)=>{this.hasOwnProperty(e)&&(this._$Et.set(e,this[e]),delete this[e])})}createRenderRoot(){var t;const e=(t=this.shadowRoot)!==null&&t!==void 0?t:this.attachShadow(this.constructor.shadowRootOptions);return St(e,this.constructor.elementStyles),e}connectedCallback(){var t;this.renderRoot===void 0&&(this.renderRoot=this.createRenderRoot()),this.enableUpdating(!0),(t=this._$Eg)===null||t===void 0||t.forEach(e=>{var s;return(s=e.hostConnected)===null||s===void 0?void 0:s.call(e)})}enableUpdating(t){}disconnectedCallback(){var t;(t=this._$Eg)===null||t===void 0||t.forEach(e=>{var s;return(s=e.hostDisconnected)===null||s===void 0?void 0:s.call(e)})}attributeChangedCallback(t,e,s){this._$AK(t,s)}_$ES(t,e,s=L){var n,r;const o=this.constructor._$Eh(t,s);if(o!==void 0&&s.reflect===!0){const c=((r=(n=s.converter)===null||n===void 0?void 0:n.toAttribute)!==null&&r!==void 0?r:V.toAttribute)(e,s.type);this._$Ei=t,c==null?this.removeAttribute(o):this.setAttribute(o,c),this._$Ei=null}}_$AK(t,e){var s,n,r;const o=this.constructor,c=o._$Eu.get(t);if(c!==void 0&&this._$Ei!==c){const l=o.getPropertyOptions(c),h=l.converter,v=(r=(n=(s=h)===null||s===void 0?void 0:s.fromAttribute)!==null&&n!==void 0?n:typeof h=="function"?h:null)!==null&&r!==void 0?r:V.fromAttribute;this._$Ei=c,this[c]=v(e,l.type),this._$Ei=null}}requestUpdate(t,e,s){let n=!0;t!==void 0&&(((s=s||this.constructor.getPropertyOptions(t)).hasChanged||ct)(this[t],e)?(this._$AL.has(t)||this._$AL.set(t,e),s.reflect===!0&&this._$Ei!==t&&(this._$EC===void 0&&(this._$EC=new Map),this._$EC.set(t,s))):n=!1),!this.isUpdatePending&&n&&(this._$Ep=this._$E_())}async _$E_(){this.isUpdatePending=!0;try{await this._$Ep}catch(e){Promise.reject(e)}const t=this.scheduleUpdate();return t!=null&&await t,!this.isUpdatePending}scheduleUpdate(){return this.performUpdate()}performUpdate(){var t;if(!this.isUpdatePending)return;this.hasUpdated,this._$Et&&(this._$Et.forEach((n,r)=>this[r]=n),this._$Et=void 0);let e=!1;const s=this._$AL;try{e=this.shouldUpdate(s),e?(this.willUpdate(s),(t=this._$Eg)===null||t===void 0||t.forEach(n=>{var r;return(r=n.hostUpdate)===null||r===void 0?void 0:r.call(n)}),this.update(s)):this._$EU()}catch(n){throw e=!1,this._$EU(),n}e&&this._$AE(s)}willUpdate(t){}_$AE(t){var e;(e=this._$Eg)===null||e===void 0||e.forEach(s=>{var n;return(n=s.hostUpdated)===null||n===void 0?void 0:n.call(s)}),this.hasUpdated||(this.hasUpdated=!0,this.firstUpdated(t)),this.updated(t)}_$EU(){this._$AL=new Map,this.isUpdatePending=!1}get updateComplete(){return this.getUpdateComplete()}getUpdateComplete(){return this._$Ep}shouldUpdate(t){return!0}update(t){this._$EC!==void 0&&(this._$EC.forEach((e,s)=>this._$ES(s,this[s],e)),this._$EC=void 0),this._$EU()}updated(t){}firstUpdated(t){}}A.finalized=!0,A.elementProperties=new Map,A.elementStyles=[],A.shadowRootOptions={mode:"open"},tt==null||tt({ReactiveElement:A}),((k=globalThis.reactiveElementVersions)!==null&&k!==void 0?k:globalThis.reactiveElementVersions=[]).push("1.3.2");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var z;const S=globalThis.trustedTypes,et=S?S.createPolicy("lit-html",{createHTML:i=>i}):void 0,f=`lit$${(Math.random()+"").slice(9)}$`,dt="?"+f,bt=`<${dt}>`,E=document,N=(i="")=>E.createComment(i),P=i=>i===null||typeof i!="object"&&typeof i!="function",ut=Array.isArray,Ct=i=>{var t;return ut(i)||typeof((t=i)===null||t===void 0?void 0:t[Symbol.iterator])=="function"},w=/<(?:(!--|\/[^a-zA-Z])|(\/?[a-zA-Z][^>\s]*)|(\/?$))/g,st=/-->/g,it=/>/g,m=/>|[ 	\n\r](?:([^\s"'>=/]+)([ 	\n\r]*=[ 	\n\r]*(?:[^ 	\n\r"'`<>=]|("|')|))|$)/g,nt=/'/g,rt=/"/g,pt=/^(?:script|style|textarea|title)$/i,wt=i=>(t,...e)=>({_$litType$:i,strings:t,values:e}),Ut=wt(1),b=Symbol.for("lit-noChange"),u=Symbol.for("lit-nothing"),ot=new WeakMap,It=(i,t,e)=>{var s,n;const r=(s=e==null?void 0:e.renderBefore)!==null&&s!==void 0?s:t;let o=r._$litPart$;if(o===void 0){const c=(n=e==null?void 0:e.renderBefore)!==null&&n!==void 0?n:null;r._$litPart$=o=new O(t.insertBefore(N(),c),c,void 0,e!=null?e:{})}return o._$AI(i),o},y=E.createTreeWalker(E,129,null,!1),Nt=(i,t)=>{const e=i.length-1,s=[];let n,r=t===2?"<svg>":"",o=w;for(let l=0;l<e;l++){const h=i[l];let v,a,d=-1,g=0;for(;g<h.length&&(o.lastIndex=g,a=o.exec(h),a!==null);)g=o.lastIndex,o===w?a[1]==="!--"?o=st:a[1]!==void 0?o=it:a[2]!==void 0?(pt.test(a[2])&&(n=RegExp("</"+a[2],"g")),o=m):a[3]!==void 0&&(o=m):o===m?a[0]===">"?(o=n!=null?n:w,d=-1):a[1]===void 0?d=-2:(d=o.lastIndex-a[2].length,v=a[1],o=a[3]===void 0?m:a[3]==='"'?rt:nt):o===rt||o===nt?o=m:o===st||o===it?o=w:(o=m,n=void 0);const M=o===m&&i[l+1].startsWith("/>")?" ":"";r+=o===w?h+bt:d>=0?(s.push(v),h.slice(0,d)+"$lit$"+h.slice(d)+f+M):h+f+(d===-2?(s.push(void 0),l):M)}const c=r+(i[e]||"<?>")+(t===2?"</svg>":"");if(!Array.isArray(i)||!i.hasOwnProperty("raw"))throw Error("invalid template strings array");return[et!==void 0?et.createHTML(c):c,s]};class T{constructor({strings:t,_$litType$:e},s){let n;this.parts=[];let r=0,o=0;const c=t.length-1,l=this.parts,[h,v]=Nt(t,e);if(this.el=T.createElement(h,s),y.currentNode=this.el.content,e===2){const a=this.el.content,d=a.firstChild;d.remove(),a.append(...d.childNodes)}for(;(n=y.nextNode())!==null&&l.length<c;){if(n.nodeType===1){if(n.hasAttributes()){const a=[];for(const d of n.getAttributeNames())if(d.endsWith("$lit$")||d.startsWith(f)){const g=v[o++];if(a.push(d),g!==void 0){const M=n.getAttribute(g.toLowerCase()+"$lit$").split(f),R=/([.?@])?(.*)/.exec(g);l.push({type:1,index:r,name:R[2],strings:M,ctor:R[1]==="."?Tt:R[1]==="?"?Mt:R[1]==="@"?Rt:H})}else l.push({type:6,index:r})}for(const d of a)n.removeAttribute(d)}if(pt.test(n.tagName)){const a=n.textContent.split(f),d=a.length-1;if(d>0){n.textContent=S?S.emptyScript:"";for(let g=0;g<d;g++)n.append(a[g],N()),y.nextNode(),l.push({type:2,index:++r});n.append(a[d],N())}}}else if(n.nodeType===8)if(n.data===dt)l.push({type:2,index:r});else{let a=-1;for(;(a=n.data.indexOf(f,a+1))!==-1;)l.push({type:7,index:r}),a+=f.length-1}r++}}static createElement(t,e){const s=E.createElement("template");return s.innerHTML=t,s}}function C(i,t,e=i,s){var n,r,o,c;if(t===b)return t;let l=s!==void 0?(n=e._$Cl)===null||n===void 0?void 0:n[s]:e._$Cu;const h=P(t)?void 0:t._$litDirective$;return(l==null?void 0:l.constructor)!==h&&((r=l==null?void 0:l._$AO)===null||r===void 0||r.call(l,!1),h===void 0?l=void 0:(l=new h(i),l._$AT(i,e,s)),s!==void 0?((o=(c=e)._$Cl)!==null&&o!==void 0?o:c._$Cl=[])[s]=l:e._$Cu=l),l!==void 0&&(t=C(i,l._$AS(i,t.values),l,s)),t}class Pt{constructor(t,e){this.v=[],this._$AN=void 0,this._$AD=t,this._$AM=e}get parentNode(){return this._$AM.parentNode}get _$AU(){return this._$AM._$AU}p(t){var e;const{el:{content:s},parts:n}=this._$AD,r=((e=t==null?void 0:t.creationScope)!==null&&e!==void 0?e:E).importNode(s,!0);y.currentNode=r;let o=y.nextNode(),c=0,l=0,h=n[0];for(;h!==void 0;){if(c===h.index){let v;h.type===2?v=new O(o,o.nextSibling,this,t):h.type===1?v=new h.ctor(o,h.name,h.strings,this,t):h.type===6&&(v=new xt(o,this,t)),this.v.push(v),h=n[++l]}c!==(h==null?void 0:h.index)&&(o=y.nextNode(),c++)}return r}m(t){let e=0;for(const s of this.v)s!==void 0&&(s.strings!==void 0?(s._$AI(t,s,e),e+=s.strings.length-2):s._$AI(t[e])),e++}}class O{constructor(t,e,s,n){var r;this.type=2,this._$AH=u,this._$AN=void 0,this._$AA=t,this._$AB=e,this._$AM=s,this.options=n,this._$Cg=(r=n==null?void 0:n.isConnected)===null||r===void 0||r}get _$AU(){var t,e;return(e=(t=this._$AM)===null||t===void 0?void 0:t._$AU)!==null&&e!==void 0?e:this._$Cg}get parentNode(){let t=this._$AA.parentNode;const e=this._$AM;return e!==void 0&&t.nodeType===11&&(t=e.parentNode),t}get startNode(){return this._$AA}get endNode(){return this._$AB}_$AI(t,e=this){t=C(this,t,e),P(t)?t===u||t==null||t===""?(this._$AH!==u&&this._$AR(),this._$AH=u):t!==this._$AH&&t!==b&&this.$(t):t._$litType$!==void 0?this.T(t):t.nodeType!==void 0?this.k(t):Ct(t)?this.S(t):this.$(t)}M(t,e=this._$AB){return this._$AA.parentNode.insertBefore(t,e)}k(t){this._$AH!==t&&(this._$AR(),this._$AH=this.M(t))}$(t){this._$AH!==u&&P(this._$AH)?this._$AA.nextSibling.data=t:this.k(E.createTextNode(t)),this._$AH=t}T(t){var e;const{values:s,_$litType$:n}=t,r=typeof n=="number"?this._$AC(t):(n.el===void 0&&(n.el=T.createElement(n.h,this.options)),n);if(((e=this._$AH)===null||e===void 0?void 0:e._$AD)===r)this._$AH.m(s);else{const o=new Pt(r,this),c=o.p(this.options);o.m(s),this.k(c),this._$AH=o}}_$AC(t){let e=ot.get(t.strings);return e===void 0&&ot.set(t.strings,e=new T(t)),e}S(t){ut(this._$AH)||(this._$AH=[],this._$AR());const e=this._$AH;let s,n=0;for(const r of t)n===e.length?e.push(s=new O(this.M(N()),this.M(N()),this,this.options)):s=e[n],s._$AI(r),n++;n<e.length&&(this._$AR(s&&s._$AB.nextSibling,n),e.length=n)}_$AR(t=this._$AA.nextSibling,e){var s;for((s=this._$AP)===null||s===void 0||s.call(this,!1,!0,e);t&&t!==this._$AB;){const n=t.nextSibling;t.remove(),t=n}}setConnected(t){var e;this._$AM===void 0&&(this._$Cg=t,(e=this._$AP)===null||e===void 0||e.call(this,t))}}class H{constructor(t,e,s,n,r){this.type=1,this._$AH=u,this._$AN=void 0,this.element=t,this.name=e,this._$AM=n,this.options=r,s.length>2||s[0]!==""||s[1]!==""?(this._$AH=Array(s.length-1).fill(new String),this.strings=s):this._$AH=u}get tagName(){return this.element.tagName}get _$AU(){return this._$AM._$AU}_$AI(t,e=this,s,n){const r=this.strings;let o=!1;if(r===void 0)t=C(this,t,e,0),o=!P(t)||t!==this._$AH&&t!==b,o&&(this._$AH=t);else{const c=t;let l,h;for(t=r[0],l=0;l<r.length-1;l++)h=C(this,c[s+l],e,l),h===b&&(h=this._$AH[l]),o||(o=!P(h)||h!==this._$AH[l]),h===u?t=u:t!==u&&(t+=(h!=null?h:"")+r[l+1]),this._$AH[l]=h}o&&!n&&this.C(t)}C(t){t===u?this.element.removeAttribute(this.name):this.element.setAttribute(this.name,t!=null?t:"")}}class Tt extends H{constructor(){super(...arguments),this.type=3}C(t){this.element[this.name]=t===u?void 0:t}}const Ot=S?S.emptyScript:"";class Mt extends H{constructor(){super(...arguments),this.type=4}C(t){t&&t!==u?this.element.setAttribute(this.name,Ot):this.element.removeAttribute(this.name)}}class Rt extends H{constructor(t,e,s,n,r){super(t,e,s,n,r),this.type=5}_$AI(t,e=this){var s;if((t=(s=C(this,t,e,0))!==null&&s!==void 0?s:u)===b)return;const n=this._$AH,r=t===u&&n!==u||t.capture!==n.capture||t.once!==n.once||t.passive!==n.passive,o=t!==u&&(n===u||r);r&&this.element.removeEventListener(this.name,this,n),o&&this.element.addEventListener(this.name,this,t),this._$AH=t}handleEvent(t){var e,s;typeof this._$AH=="function"?this._$AH.call((s=(e=this.options)===null||e===void 0?void 0:e.host)!==null&&s!==void 0?s:this.element,t):this._$AH.handleEvent(t)}}class xt{constructor(t,e,s){this.element=t,this.type=6,this._$AN=void 0,this._$AM=e,this.options=s}get _$AU(){return this._$AM._$AU}_$AI(t){C(this,t)}}const lt=window.litHtmlPolyfillSupport;lt==null||lt(T,O),((z=globalThis.litHtmlVersions)!==null&&z!==void 0?z:globalThis.litHtmlVersions=[]).push("2.2.3");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var G,D;class U extends A{constructor(){super(...arguments),this.renderOptions={host:this},this._$Dt=void 0}createRenderRoot(){var t,e;const s=super.createRenderRoot();return(t=(e=this.renderOptions).renderBefore)!==null&&t!==void 0||(e.renderBefore=s.firstChild),s}update(t){const e=this.render();this.hasUpdated||(this.renderOptions.isConnected=this.isConnected),super.update(t),this._$Dt=It(e,this.renderRoot,this.renderOptions)}connectedCallback(){var t;super.connectedCallback(),(t=this._$Dt)===null||t===void 0||t.setConnected(!0)}disconnectedCallback(){var t;super.disconnectedCallback(),(t=this._$Dt)===null||t===void 0||t.setConnected(!1)}render(){return b}}U.finalized=!0,U._$litElement$=!0,(G=globalThis.litElementHydrateSupport)===null||G===void 0||G.call(globalThis,{LitElement:U});const ht=globalThis.litElementPolyfillSupport;ht==null||ht({LitElement:U});((D=globalThis.litElementVersions)!==null&&D!==void 0?D:globalThis.litElementVersions=[]).push("3.2.0");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Ht=i=>t=>typeof t=="function"?((e,s)=>(window.customElements.define(e,s),s))(i,t):((e,s)=>{const{kind:n,elements:r}=s;return{kind:n,elements:r,finisher(o){window.customElements.define(e,o)}}})(i,t);/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const kt=(i,t)=>t.kind==="method"&&t.descriptor&&!("value"in t.descriptor)?Q(J({},t),{finisher(e){e.createProperty(t.key,i)}}):{kind:"field",key:Symbol(),placement:"own",descriptor:{},originalKey:t.key,initializer(){typeof t.initializer=="function"&&(this[t.key]=t.initializer.call(this))},finisher(e){e.createProperty(t.key,i)}};function _(i){return(t,e)=>e!==void 0?((s,n,r)=>{n.constructor.createProperty(r,s)})(i,t,e):kt(i,t)}/**
 * @license
 * Copyright 2021 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var B;((B=window.HTMLSlotElement)===null||B===void 0?void 0:B.prototype.assignedElements)!=null;/**
 * @license
 * Copyright 2020 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Lt=i=>i.strings===void 0;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const zt={ATTRIBUTE:1,CHILD:2,PROPERTY:3,BOOLEAN_ATTRIBUTE:4,EVENT:5,ELEMENT:6},Gt=i=>(...t)=>({_$litDirective$:i,values:t});class Dt{constructor(t){}get _$AU(){return this._$AM._$AU}_$AT(t,e,s){this._$Ct=t,this._$AM=e,this._$Ci=s}_$AS(t,e){return this.update(t,e)}update(t,e){return this.render(...e)}}/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const I=(i,t)=>{var e,s;const n=i._$AN;if(n===void 0)return!1;for(const r of n)(s=(e=r)._$AO)===null||s===void 0||s.call(e,t,!1),I(r,t);return!0},x=i=>{let t,e;do{if((t=i._$AM)===void 0)break;e=t._$AN,e.delete(i),i=t}while((e==null?void 0:e.size)===0)},$t=i=>{for(let t;t=i._$AM;i=t){let e=t._$AN;if(e===void 0)t._$AN=e=new Set;else if(e.has(i))break;e.add(i),Vt(t)}};function Bt(i){this._$AN!==void 0?(x(this),this._$AM=i,$t(this)):this._$AM=i}function jt(i,t=!1,e=0){const s=this._$AH,n=this._$AN;if(n!==void 0&&n.size!==0)if(t)if(Array.isArray(s))for(let r=e;r<s.length;r++)I(s[r],!1),x(s[r]);else s!=null&&(I(s,!1),x(s));else I(this,i)}const Vt=i=>{var t,e,s,n;i.type==zt.CHILD&&((t=(s=i)._$AP)!==null&&t!==void 0||(s._$AP=jt),(e=(n=i)._$AQ)!==null&&e!==void 0||(n._$AQ=Bt))};class Kt extends Dt{constructor(){super(...arguments),this._$AN=void 0}_$AT(t,e,s){super._$AT(t,e,s),$t(this),this.isConnected=t._$AU}_$AO(t,e=!0){var s,n;t!==this.isConnected&&(this.isConnected=t,t?(s=this.reconnected)===null||s===void 0||s.call(this):(n=this.disconnected)===null||n===void 0||n.call(this)),e&&(I(this,t),x(this))}setValue(t){if(Lt(this._$Ct))this._$Ct._$AI(t,this);else{const e=[...this._$Ct._$AH];e[this._$Ci]=t,this._$Ct._$AI(e,this,0)}}disconnected(){}reconnected(){}}/**
 * @license
 * Copyright 2020 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Wt=()=>new Yt;class Yt{}const j=new WeakMap,qt=Gt(class extends Kt{render(i){return u}update(i,[t]){var e;const s=t!==this.U;return s&&this.U!==void 0&&this.ot(void 0),(s||this.rt!==this.lt)&&(this.U=t,this.ht=(e=i.options)===null||e===void 0?void 0:e.host,this.ot(this.lt=i.element)),u}ot(i){var t;if(typeof this.U=="function"){const e=(t=this.ht)!==null&&t!==void 0?t:globalThis;let s=j.get(e);s===void 0&&(s=new WeakMap,j.set(e,s)),s.get(this.U)!==void 0&&this.U.call(this.ht,void 0),s.set(this.U,i),i!==void 0&&this.U.call(this.ht,i)}else this.U.value=i}get rt(){var i,t,e;return typeof this.U=="function"?(t=j.get((i=this.ht)!==null&&i!==void 0?i:globalThis))===null||t===void 0?void 0:t.get(this.U):(e=this.U)===null||e===void 0?void 0:e.value}disconnected(){this.rt===this.lt&&this.ot(void 0)}reconnected(){this.ot(this.lt)}});var Jt=Object.defineProperty,Qt=Object.getOwnPropertyDescriptor,$=(i,t,e,s)=>{for(var n=s>1?void 0:s?Qt(t,e):t,r=i.length-1,o;r>=0;r--)(o=i[r])&&(n=(s?o(t,e,n):o(n))||n);return s&&n&&Jt(t,e,n),n};let p=class extends U{constructor(){super(),this.GISCUS_SESSION_KEY="giscus-session",this.GISCUS_ORIGIN="https://giscus.app",this.ERROR_SUGGESTION="Please consider reporting this error at https://github.com/giscus/giscus/issues/new.",this.__session="",this._iframeRef=Wt(),this.messageEventHandler=this.handleMessageEvent.bind(this),this.reactionsEnabled="1",this.emitMetadata="0",this.inputPosition="bottom",this.theme="light",this.lang="en",this.loading="eager",this.setupSession(),window.addEventListener("message",this.messageEventHandler)}get iframeRef(){return this._iframeRef.value}connectedCallback(){super.connectedCallback()}disconnectedCallback(){super.disconnectedCallback(),window.removeEventListener("message",this.messageEventHandler)}_formatError(i){return`[giscus] An error occurred. Error message: "${i}".`}setupSession(){const i=location.href,t=new URL(i),e=localStorage.getItem(this.GISCUS_SESSION_KEY),s=t.searchParams.get("giscus")||"";if(s){localStorage.setItem(this.GISCUS_SESSION_KEY,JSON.stringify(s)),this.__session=s,t.searchParams.delete("giscus"),history.replaceState(void 0,document.title,t.toString());return}if(e)try{this.__session=JSON.parse(e||"")||""}catch(n){this.__session="",localStorage.removeItem(this.GISCUS_SESSION_KEY),console.warn(`${this._formatError(n==null?void 0:n.message)} Session has been cleared.`)}}handleMessageEvent(i){if(i.origin!==this.GISCUS_ORIGIN)return;const{data:t}=i;if(!(typeof t=="object"&&t.giscus)||(this.iframeRef&&t.giscus.resizeHeight&&(this.iframeRef.style.height=`${t.giscus.resizeHeight}px`),!t.giscus.error))return;const e=t.giscus.error;if(e.includes("Bad credentials")||e.includes("Invalid state value")){if(localStorage.getItem(this.GISCUS_SESSION_KEY)!==null){localStorage.removeItem(this.GISCUS_SESSION_KEY),this.__session="",console.warn(`${this._formatError(e)} Session has been cleared.`),this.update(new Map);return}console.error(`${this._formatError(e)} No session is stored initially. ${this.ERROR_SUGGESTION}`)}if(e.includes("Discussion not found")){console.warn(`[giscus] ${e}. A new discussion will be created if a comment/reaction is submitted.`);return}console.error(`${this._formatError(e)} ${this.ERROR_SUGGESTION}`)}sendMessage(i){var t,e;(e=(t=this.iframeRef)==null?void 0:t.contentWindow)==null||e.postMessage({giscus:i},this.GISCUS_ORIGIN)}updateConfig(){const i={setConfig:{repo:this.repo,repoId:this.repoId,category:this.category,categoryId:this.categoryId,term:this.getTerm(),number:+this.getNumber(),reactionsEnabled:this.reactionsEnabled==="1",emitMetadata:this.emitMetadata==="1",inputPosition:this.inputPosition,theme:this.theme,lang:this.lang}};this.sendMessage(i)}requestUpdate(i,t,e){if(!this.hasUpdated){super.requestUpdate(i,t,e);return}this.updateConfig()}_getOgMetaContent(i){const t=document.querySelector(`meta[property='og:${i}'],meta[name='${i}']`);return t?t.content:""}_getCleanedUrl(){const i=new URL(location.href);return i.searchParams.delete("giscus"),i}getTerm(){switch(this.mapping){case"url":return`${this._getCleanedUrl()}`;case"title":return document.title;case"og:title":return this._getOgMetaContent("title");case"specific":return this.term||"";case"number":return"";case"pathname":default:return location.pathname.length<2?"index":location.pathname.substring(1).replace(/\.\w+$/,"")}}getNumber(){return this.mapping==="number"&&this.term||""}getIframeSrc(){const t=`${this._getCleanedUrl()}${this.id?"#"+this.id:""}`,e=this._getOgMetaContent("description"),s={origin:t,session:this.__session,repo:this.repo,repoId:this.repoId||"",category:this.category||"",categoryId:this.categoryId||"",term:this.getTerm(),number:this.getNumber(),reactionsEnabled:this.reactionsEnabled,emitMetadata:this.emitMetadata,inputPosition:this.inputPosition,theme:this.theme,description:e},n=this.lang?`/${this.lang}`:"",r=new URLSearchParams(s);return`${this.GISCUS_ORIGIN}${n}/widget?${r}`}render(){return Ut`
      <iframe
        scrolling="no"
        ${qt(this._iframeRef)}
        src=${this.getIframeSrc()}
        loading=${this.loading}
        part="iframe"
      ></iframe>
    `}};p.styles=yt`
    :host,
    iframe {
      width: 100%;
      border: none;
      color-scheme: normal;
      min-height: 150px;
    }
  `;$([_({reflect:!0})],p.prototype,"repo",2);$([_({reflect:!0})],p.prototype,"repoId",2);$([_({reflect:!0})],p.prototype,"category",2);$([_({reflect:!0})],p.prototype,"categoryId",2);$([_({reflect:!0})],p.prototype,"mapping",2);$([_({reflect:!0})],p.prototype,"term",2);$([_({reflect:!0})],p.prototype,"reactionsEnabled",2);$([_({reflect:!0})],p.prototype,"emitMetadata",2);$([_({reflect:!0})],p.prototype,"inputPosition",2);$([_({reflect:!0})],p.prototype,"theme",2);$([_({reflect:!0})],p.prototype,"lang",2);$([_({reflect:!0})],p.prototype,"loading",2);p=$([Ht("giscus-widget")],p);export{p as GiscusWidget};
