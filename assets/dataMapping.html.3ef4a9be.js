import{e as a}from"./app.fb15937b.js";import{_ as n}from"./plugin-vue_export-helper.21dcd24c.js";var e="/fast-request/assets/customDataMapping_en.5bf05592.png",s="/fast-request/assets/defaultDataMapping_en.c812b05c.png",t="/fast-request/assets/ignoreDataMapping_en.aa55d88e.png";const p={},i=a(`<h2 id="custom-type-mapping" tabindex="-1"><a class="header-anchor" href="#custom-type-mapping" aria-hidden="true">#</a> Custom type mapping</h2><p>Scenes:there are 10 attributes in<code>com.baomidou.mybatisplus.extension.plugins.pagination.Page</code>,but we just need size and current property</p><p>A total of 2 values need to be set for custom type mapping</p><p><strong>Java Type</strong> is the corresponding object type,it must contain package name and class name</p><div class="language-java ext-java line-numbers-mode"><pre class="language-java"><code><span class="token class-name"><span class="token namespace">com<span class="token punctuation">.</span>baomidou<span class="token punctuation">.</span>mybatisplus<span class="token punctuation">.</span>extension<span class="token punctuation">.</span>plugins<span class="token punctuation">.</span>pagination<span class="token punctuation">.</span></span>Page</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><p><strong>Default value</strong> must be in json format</p><div class="language-json ext-json line-numbers-mode"><pre class="language-json"><code><span class="token punctuation">{</span><span class="token property">&quot;size&quot;</span><span class="token operator">:</span><span class="token number">10</span><span class="token punctuation">,</span><span class="token property">&quot;current&quot;</span><span class="token operator">:</span><span class="token number">1</span><span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><p><img src="`+e+'" alt="" loading="lazy"></p><h2 id="default-type-mapping" tabindex="-1"><a class="header-anchor" href="#default-type-mapping" aria-hidden="true">#</a> Default type mapping</h2><p>This configuration determines that the java basic type is parsed into the corresponding value\uFF0Csupport modification</p><p><img src="'+s+'" alt="" loading="lazy"></p><h2 id="ignore-data-mapping" tabindex="-1"><a class="header-anchor" href="#ignore-data-mapping" aria-hidden="true">#</a> Ignore data mapping</h2><p>This configuration determines whether the class will be parsed, it need full class name path(packageName+className)</p><p><img src="'+t+'" alt="" loading="lazy"></p>',14);function o(r,c){return i}var d=n(p,[["render",o],["__file","dataMapping.html.vue"]]);export{d as default};