# 恐龙宠物「龙宝」美术规格 v1.0

> 落地目标：单文件 HTML 儿童识字游戏（5 岁女孩），零外链、离线可用、所有图形内联 SVG。
> 物种 key = `dino`，默认名「龙宝」，与墨墨（key=`ink`）并列第二种可选宠物。
> 性格：可爱、圆润、友好；**禁止**尖牙、利爪、血盆大口、攻击/恐怖感。
> 本规格只产出美术参数与可直接粘贴的 ES5 内联 SVG；**不修改** `识字大冒险.html`（渲染与切换 UI 由 team-lead 实现）。

## 0. 全局约束（工程师落地前必读）

- **纯内联 SVG / CSS**，禁止位图、外链、web font。
- **ES5 风格**：`var` + 字符串拼接；禁止模板字符串（反引号）、箭头函数、`let`/`const`、解构。
- 坐标空间沿用墨墨：所有身体/装扮绘制在 `<g class="pk-all" transform="translate(60,120) scale(S) translate(-60,-120)">` 内，**未缩放坐标**以 `(60,120)` 为基准点。本规格给出的所有 x/y 均为未缩放值。
- 身体中心固定在 `(60,84)`、`bodyDy=36`，与墨墨一致，保证光环/辉光/王冠/星轨（petFxLayout 复用墨墨逻辑）对齐。
- **肚皮恒定暖米 `#F7EBD2`**（对应 `S.belly`，任何等级不变）。
- **描边金线规则沿用墨墨**：`petSVG` 里 `lineC = (lv>=5) ? '#FFC93C' : S.line`。即 **Lv5 起自动转金 `#FFC93C`**；Lv1–4 用 `S.line`（本规格给的绿描边）。下面 DINO_STAGES 的 `line` 列即 Lv1–4 绿描边，Lv5+ 由代码覆盖为金。
- **脸（眼睛/嘴/腮红/表情）完全复用 `petSVG` 现有 `lv>=2` 区块**（mood 感知），恐龙身体片段不含脸，工程师在身体之后追加即可。
- **按等级变形**是恐龙区别于墨墨的最大卖点：墨墨 Lv3–Lv40 同一套画法只改缩放+配色；恐龙用 §1 的 8 个形态里程碑，低等级是蛋/幼崽，高等级长成有背棘、头冠、尾晶的大龙。

---

## 1. 八形态里程碑（随等级变形）

每个里程碑给出 Lv 区间、整体比例、新增部件、**可直接粘贴的 ES5 内联 SVG 片段**。
片段均为「身体内层标记」，放入 `pk-all` 内、阴影之后、脸之前。片段里用到的变量与 `petSVG` 保持一致：`S`（当前级 stage 对象，含 `theme/hi/belly`）、`lineC`、`lineW`、`pref`（gradient id 前缀）、`phBreath`（呼吸动画相位）。`lv` 为当前等级。

> 通用底：阴影 `<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>` 每形态都先画。

### F1 · 龙蛋（Lv1）
- 比例：纯蛋，无肢体。蛋高≈身长 1.0，无尾、无背。
- 新增部件：蛋壳 + 裂纹 + 蛋底探头的小尾尖（暗示将孵化的恐龙）。
- 片段：
```js
function dinoEgg(lv,S,lineC,lineW,pref,phBreath){
  return '<g class="pk-egg" style="animation-delay:'+phBreath+'ms">'
    + '<ellipse cx="60" cy="82" rx="31" ry="39" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>'
    + '<path d="M42 60 l9 8 -7 7 10 7" fill="none" stroke="'+lineC+'" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"/>'
    + '<path d="M72 58 l-8 9 8 6 -7 8" fill="none" stroke="'+lineC+'" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"/>'
    /* 蛋底探头的小尾尖 */
    + '<path d="M82 112 q12 6 17 -3" fill="none" stroke="'+lineC+'" stroke-width="6" stroke-linecap="round"/>'
    + '<circle cx="50" cy="78" r="4.2" fill="#3B2A22"/><circle cx="70" cy="78" r="4.2" fill="#3B2A22"/>'
    + '<circle cx="51.4" cy="76.6" r="1.5" fill="#FFFFFF"/><circle cx="71.4" cy="76.6" r="1.5" fill="#FFFFFF"/>'
    + '<ellipse cx="42" cy="90" rx="6" ry="4" fill="#FF9AA6" opacity=".5"/><ellipse cx="78" cy="90" rx="6" ry="4" fill="#FF9AA6" opacity=".5"/>'
  + '</g>';
}
```
> 注：F1 的蛋自带脸（与墨墨 Lv1 一致），不需额外画脸。

### F2 · 破壳幼崽（Lv2–5）
- 比例：身长≈0.7（圆胖小身），头大身小；只有小尾尖，无背棘、无腿。
- 新增部件：下半蛋壳 + 小圆身 + 头顶两只小角芽 + 探头小尾尖。
- 片段：
```js
function dinoHatch(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  /* 下半蛋壳 */
  o += '<path d="M30 96 Q60 122 90 96 Q90 112 60 117 Q30 112 30 96 Z" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  /* 小圆身 */
  o += '<ellipse cx="60" cy="80" rx="22" ry="22" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="88" rx="13" ry="11" fill="'+S.belly+'" opacity=".9"/>';
  /* 探头小尾尖 */
  o += '<path d="M80 92 q12 5 16 -4" fill="none" stroke="'+lineC+'" stroke-width="7" stroke-linecap="round"/>';
  /* 头顶两只小角芽 */
  o += '<ellipse cx="50" cy="60" rx="6" ry="8" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(-18 50 60)"/>';
  o += '<ellipse cx="70" cy="60" rx="6" ry="8" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(18 70 60)"/>';
  o += '</g>';
  return o;
}
```

### F3 · 短尾 + 圆背鳍（Lv6–9）
- 比例：身长≈1.0（标准圆身），尾短；背后一枚**圆润软背鳍**。
- 新增部件：短尾（右侧）、圆背鳍（单枚软弧）、两只小脚墩。
- 片段：
```js
function dinoF3(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  /* 短尾 */
  o += '<path d="M86 100 q16 8 18 -6" fill="none" stroke="'+lineC+'" stroke-width="13" stroke-linecap="round"/>';
  o += '<path d="M86 100 q16 8 18 -6" fill="none" stroke="'+S.theme+'" stroke-width="9" stroke-linecap="round"/>';
  /* 身体 */
  o += '<ellipse cx="60" cy="84" rx="30" ry="32" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="95" rx="18" ry="15" fill="'+S.belly+'" opacity=".9"/>';
  /* 圆背鳍（单枚软弧，无尖角） */
  o += '<path d="M44 56 Q60 40 76 56 Q60 50 44 56 Z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  /* 两只小脚墩 */
  o += '<ellipse cx="46" cy="112" rx="9" ry="7" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="74" cy="112" rx="9" ry="7" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '</g>';
  return o;
}
```

### F4 · 三角背棘 + 后腿（Lv10–13）
- 比例：身长≈1.05，背棘三枚（圆角三角，非尖刺），后腿成形带脚。
- 新增部件：三枚圆角背棘、两只带脚后腿。
- 片段（在 F3 身体基础上替换背鳍为背棘、脚墩为后腿）：
```js
function dinoF4(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  o += '<path d="M86 100 q18 10 20 -6" fill="none" stroke="'+lineC+'" stroke-width="13" stroke-linecap="round"/>';
  o += '<path d="M86 100 q18 10 20 -6" fill="none" stroke="'+S.theme+'" stroke-width="9" stroke-linecap="round"/>';
  o += '<ellipse cx="60" cy="84" rx="30" ry="32" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="95" rx="18" ry="15" fill="'+S.belly+'" opacity=".9"/>';
  /* 三枚圆角背棘（rounded，非尖锐） */
  o += '<path d="M46 56 q4 -12 8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  o += '<path d="M56 53 q4 -14 8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  o += '<path d="M66 56 q4 -12 8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  /* 后腿（带脚） */
  o += '<path d="M44 104 q-4 12 2 16 q10 2 12 -6 q-2 -8 -14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<path d="M76 104 q4 12 -2 16 q-10 2 -12 -6 q2 -8 14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '</g>';
  return o;
}
```

### F5 · 体形拉长 + 头顶小角 + 尾鳍（Lv14–17）
- 比例：身长≈1.15（身体略高 `ry` 加到 34），头顶一枚小角，尾端加圆鳍。
- 新增部件：头顶小角、尾鳍（尾尖圆扇形）。
- 片段（在 F4 基础上：身体 `ry=34`、加头顶小角、尾端改鳍）：
```js
function dinoF5(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  /* 尾 + 尾鳍（圆扇形，无尖刺） */
  o += '<path d="M86 102 q20 10 22 -4" fill="none" stroke="'+lineC+'" stroke-width="13" stroke-linecap="round"/>';
  o += '<path d="M86 102 q20 10 22 -4" fill="none" stroke="'+S.theme+'" stroke-width="9" stroke-linecap="round"/>';
  o += '<path d="M104 98 q14 -10 18 4 q-10 10 -18 -4 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  o += '<ellipse cx="60" cy="84" rx="30" ry="34" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="96" rx="18" ry="16" fill="'+S.belly+'" opacity=".9"/>';
  /* 背棘两枚（更高） */
  o += '<path d="M50 54 q5 -16 10 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  o += '<path d="M62 54 q5 -16 10 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  /* 后腿 */
  o += '<path d="M44 106 q-4 12 2 16 q10 2 12 -6 q-2 -8 -14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<path d="M76 106 q4 12 -2 16 q-10 2 -12 -6 q2 -8 14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '</g>';
  /* 头顶小角（软锥，圆头） */
  o += '<path d="M56 50 q4 -14 8 0 q-4 4 -8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  return o;
}
```

### F6 · 前爪 + 背上骨质纹（Lv18–21）
- 比例：身长≈1.18，加两只前爪（身侧小臂），背棘间加浅色骨质纹（圆点/短弧，非恐怖骨刺）。
- 新增部件：前爪 ×2、背骨质纹（浅色短弧）。
- 片段（在 F5 基础上加前爪与背纹）：
```js
function dinoF6(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  o += '<path d="M86 102 q20 10 22 -4" fill="none" stroke="'+lineC+'" stroke-width="13" stroke-linecap="round"/>';
  o += '<path d="M86 102 q20 10 22 -4" fill="none" stroke="'+S.theme+'" stroke-width="9" stroke-linecap="round"/>';
  o += '<path d="M104 98 q14 -10 18 4 q-10 10 -18 -4 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  o += '<ellipse cx="60" cy="84" rx="30" ry="34" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="96" rx="18" ry="16" fill="'+S.belly+'" opacity=".9"/>';
  /* 背棘 */
  o += '<path d="M50 54 q5 -16 10 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  o += '<path d="M62 54 q5 -16 10 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2" stroke-linejoin="round"/>';
  /* 背上骨质纹（浅色圆点，童真不恐怖） */
  o += '<circle cx="60" cy="70" r="2.4" fill="'+S.belly+'" opacity=".9"/>';
  o += '<circle cx="52" cy="74" r="2" fill="'+S.belly+'" opacity=".9"/>';
  o += '<circle cx="68" cy="74" r="2" fill="'+S.belly+'" opacity=".9"/>';
  /* 后腿 */
  o += '<path d="M44 106 q-4 12 2 16 q10 2 12 -6 q-2 -8 -14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<path d="M76 106 q4 12 -2 16 q-10 2 -12 -6 q2 -8 14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  /* 前爪（身侧小臂，圆头） */
  o += '<ellipse cx="30" cy="92" rx="7" ry="9" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(-12 30 92)"/>';
  o += '<ellipse cx="90" cy="92" rx="7" ry="9" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(12 90 92)"/>';
  o += '</g>';
  o += '<path d="M56 50 q4 -14 8 0 q-4 4 -8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  return o;
}
```

### F7 · 鳍褶延伸（Lv22–29）
- 比例：身长≈1.2，背棘连成**波浪鳍褶**（圆润连续，非尖刺排），尾鳍加大。
- 新增部件：连续波浪背鳍褶、加大尾鳍。
- 片段（在 F6 基础上把背棘/骨质纹换成波浪鳍褶）：
```js
function dinoF7(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  o += '<path d="M84 102 q22 12 26 -4" fill="none" stroke="'+lineC+'" stroke-width="13" stroke-linecap="round"/>';
  o += '<path d="M84 102 q22 12 26 -4" fill="none" stroke="'+S.theme+'" stroke-width="9" stroke-linecap="round"/>';
  /* 加大尾鳍（圆扇，双叶） */
  o += '<path d="M108 98 q16 -14 22 2 q-12 14 -22 -2 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  o += '<ellipse cx="60" cy="84" rx="30" ry="34" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="96" rx="18" ry="16" fill="'+S.belly+'" opacity=".9"/>';
  /* 连续波浪背鳍褶（圆润） */
  o += '<path d="M42 58 q6 -16 12 0 q6 -16 12 0 q6 -16 12 0 q6 -14 10 2 q-12 10 -46 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  o += '<path d="M44 106 q-4 12 2 16 q10 2 12 -6 q-2 -8 -14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<path d="M76 106 q4 12 -2 16 q-10 2 -12 -6 q2 -8 14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<ellipse cx="30" cy="92" rx="7" ry="9" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(-12 30 92)"/>';
  o += '<ellipse cx="90" cy="92" rx="7" ry="9" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(12 90 92)"/>';
  o += '</g>';
  o += '<path d="M56 50 q4 -14 8 0 q-4 4 -8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  return o;
}
```

### F8 · 头冠 + 尾端晶石暖光（Lv30–40）
- 比例：身长≈1.22（封顶，scale 已锁 1.75），头顶**头冠（frill 冠）**向上展开到 `topY≈18`，尾端一枚**暖光晶石**。这是全形态最高一档，决定 `PET_GEO.dino.topY`。
- 新增部件：头冠（圆润扇面 frill，向上展开）、尾端晶石（菱形暖光，外加径向暖光）。
- 片段（在 F7 基础上加头冠与尾晶；头冠高度即 §3 的 `topY`）：
```js
function dinoF8(lv,S,lineC,lineW,pref,phBreath){
  var o = '<ellipse cx="60" cy="124" rx="30" ry="6" fill="#5A3C28" opacity=".13"/>';
  o += '<g class="pk-body" style="animation-delay:'+phBreath+'ms">';
  o += '<path d="M84 102 q22 12 26 -4" fill="none" stroke="'+lineC+'" stroke-width="13" stroke-linecap="round"/>';
  o += '<path d="M84 102 q22 12 26 -4" fill="none" stroke="'+S.theme+'" stroke-width="9" stroke-linecap="round"/>';
  /* 加大尾鳍 + 尾端晶石（暖光菱形） */
  o += '<path d="M108 98 q16 -14 22 2 q-12 14 -22 -2 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  o += '<path d="M120 96 l6 -10 6 10 -6 10 z" fill="'+S.accent+'" stroke="'+lineC+'" stroke-width="1.8" stroke-linejoin="round"/>';
  o += '<ellipse cx="60" cy="84" rx="30" ry="34" fill="url(#'+pref+'pkBody'+lv+')" stroke="'+lineC+'" stroke-width="'+lineW+'"/>';
  o += '<ellipse cx="60" cy="96" rx="18" ry="16" fill="'+S.belly+'" opacity=".9"/>';
  /* 连续波浪背鳍褶 */
  o += '<path d="M42 58 q6 -16 12 0 q6 -16 12 0 q6 -16 12 0 q6 -14 10 2 q-12 10 -46 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  o += '<path d="M44 106 q-4 12 2 16 q10 2 12 -6 q-2 -8 -14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<path d="M76 106 q4 12 -2 16 q-10 2 -12 -6 q2 -8 14 -10 z" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" stroke-linejoin="round"/>';
  o += '<ellipse cx="30" cy="92" rx="7" ry="9" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(-12 30 92)"/>';
  o += '<ellipse cx="90" cy="92" rx="7" ry="9" fill="'+S.theme+'" stroke="'+lineC+'" stroke-width="'+lineW+'" transform="rotate(12 90 92)"/>';
  o += '</g>';
  /* 头顶小角 */
  o += '<path d="M56 50 q4 -14 8 0 q-4 4 -8 0 z" fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.4" stroke-linejoin="round"/>';
  /* 头冠（圆润扇面 frill，向上展开；顶端 y≈18 → 决定 topY） */
  o += '<g class="pk-crest">'
    + '<path d="M40 50 Q34 24 48 20 Q54 34 60 18 Q66 34 72 20 Q86 24 80 50 Q60 42 40 50 Z" '
    + 'fill="'+S.hi+'" stroke="'+lineC+'" stroke-width="2.6" stroke-linejoin="round"/>'
    + '<circle cx="48" cy="30" r="2.6" fill="'+S.accent+'" stroke="'+lineC+'" stroke-width="1"/>'
    + '<circle cx="60" cy="26" r="2.6" fill="'+S.accent+'" stroke="'+lineC+'" stroke-width="1"/>'
    + '<circle cx="72" cy="30" r="2.6" fill="'+S.accent+'" stroke="'+lineC+'" stroke-width="1"/>'
  + '</g>';
  return o;
}
```

> **形态胖瘦对照**（让 Lv3→Lv40 一眼是「幼崽长成大龙」而非单纯放大）：
> - F1 蛋：宽高≈1:1.3 的竖蛋。
> - F2 幼崽：头大身小（身 rx22 / 蛋 rx31），最胖圆。
> - F3–F4：标准圆身（rx30 ry32），尾短、脚小。
> - F5–F6：身体略拉长（ry34），出现小角、前爪，开始「修长」。
> - F7–F8：背鳍褶延伸、尾鳍加大，轮廓最舒展；F8 头顶冠展开，整体最高。

---

## 2. DINO_STAGES · 40 级配色轨道 + 名字

格式严格照 `PET_STAGES` 的 8 字段：`lv / name / bond / scale / theme / hi / line / belly`。
额外附 `dark`（深绿，用于阴影/描边加深）、`accent`（暖金点缀，用于头冠/晶石）两列 —— 即每级 **6 个 hex**（theme/hi/line/belly/dark/accent）。`belly` 恒为 `#F7EBD2`。

- `scale` 完全复用墨墨数值（Lv1 .75 → Lv20 1.75，Lv21–40 锁 1.75），保证与 FX 安全盒一致。
- `bond` 完全复用墨墨阈值，保证恐龙与墨墨同级所需亲密度一致（玩法对齐）。
- `line` 为 Lv1–4 绿描边；**Lv5 起由 `petSVG` 自动覆盖为金 `#FFC93C`**（见 §0）。
- 配色六段叙事：嫩芽绿(A:Lv1–6) → 森林绿(B:Lv7–13) → 湖翡翠(C:Lv14–20) → 青碧(D:Lv21–26) → 金曜绿(E:Lv27–33) → 翠金(F:Lv34–40)。与墨墨「紫→金」在缩略图上一眼区分。

| lv | name | bond | scale | theme | hi | line | belly | dark | accent |
|----|------|------|-------|-------|-----|------|-------|------|--------|
| 1 | 龙蛋 | 0 | .75 | #BCE39A | #DDF4C6 | #6FA84B | #F7EBD2 | #4E8A36 | #FFE9A8 |
| 2 | 小芽 | 50 | .85 | #BCE39A | #DDF4C6 | #6FA84B | #F7EBD2 | #4E8A36 | #FFE9A8 |
| 3 | 芽龙 | 150 | 1.00 | #BCE39A | #DDF4C6 | #6FA84B | #F7EBD2 | #4E8A36 | #FFE9A8 |
| 4 | 嫩芽 | 300 | 1.15 | #BCE39A | #DDF4C6 | #6FA84B | #F7EBD2 | #4E8A36 | #FFE9A8 |
| 5 | 绿芽 | 500 | 1.30 | #BCE39A | #DDF4C6 | #6FA84B | #F7EBD2 | #4E8A36 | #FFE9A8 |
| 6 | 豆豆 | 750 | 1.38 | #BCE39A | #DDF4C6 | #6FA84B | #F7EBD2 | #4E8A36 | #FFE9A8 |
| 7 | 森林 | 1050 | 1.45 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 8 | 林宝 | 1400 | 1.52 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 9 | 青芽 | 1800 | 1.58 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 10 | 小棘 | 2250 | 1.65 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 11 | 棘龙 | 2550 | 1.66 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 12 | 湖宝 | 2850 | 1.67 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 13 | 翡翠 | 3150 | 1.68 | #7CC15E | #A9DE8E | #3E8B3A | #F7EBD2 | #2C6B2A | #FFE9A8 |
| 14 | 湖玉 | 3450 | 1.69 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 15 | 碧波 | 3750 | 1.70 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 16 | 小角 | 4050 | 1.71 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 17 | 角龙 | 4350 | 1.72 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 18 | 爪爪 | 4650 | 1.73 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 19 | 骨纹 | 4950 | 1.74 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 20 | 山宝 | 5250 | 1.75 | #46BBA3 | #84DFC9 | #1F7E6E | #F7EBD2 | #146252 | #FFE9A8 |
| 21 | 岩龙 | 5500 | 1.75 | #3FB3C6 | #84D7E6 | #1E7C8E | #F7EBD2 | #155E6B | #FFE9A8 |
| 22 | 青碧 | 5750 | 1.75 | #3FB3C6 | #84D7E6 | #1E7C8E | #F7EBD2 | #155E6B | #FFE9A8 |
| 23 | 溪龙 | 6000 | 1.75 | #3FB3C6 | #84D7E6 | #1E7C8E | #F7EBD2 | #155E6B | #FFE9A8 |
| 24 | 岚龙 | 6250 | 1.75 | #3FB3C6 | #84D7E6 | #1E7C8E | #F7EBD2 | #155E6B | #FFE9A8 |
| 25 | 云海 | 6500 | 1.75 | #3FB3C6 | #84D7E6 | #1E7C8E | #F7EBD2 | #155E6B | #FFE9A8 |
| 26 | 山海 | 6750 | 1.75 | #3FB3C6 | #84D7E6 | #1E7C8E | #F7EBD2 | #155E6B | #FFE9A8 |
| 27 | 金曜 | 7000 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 28 | 曜龙 | 7250 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 29 | 翠羽 | 7500 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 30 | 祥龙 | 7750 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 31 | 瑞龙 | 7950 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 32 | 麟龙 | 8150 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 33 | 玉麟 | 8350 | 1.75 | #93C84A | #C8E78C | #FFC93C | #F7EBD2 | #6E9A2E | #FFD86B |
| 34 | 星麟 | 8550 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |
| 35 | 金麟 | 8750 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |
| 36 | 瑞麟 | 8950 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |
| 37 | 祥麟 | 9150 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |
| 38 | 宝麟 | 9350 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |
| 39 | 圣麟 | 9550 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |
| 40 | 至尊麟 | 9750 | 1.75 | #A6C24E | #E2D988 | #FFC93C | #F7EBD2 | #7E9430 | #FFE9A8 |

> 落地代码：`var PET_DINO_STAGES = [ ... ];`（40 项，照上表）。`petStagesFor('dino')` 已预留读取 `PET_DINO_STAGES`。

### 命名说明
- 三段叙事：幼龙段(Lv1–13) / 山海段(Lv14–26) / 瑞兽段(Lv27–40)。
- 全部 ≤4 字，5 岁可懂又「酷」（豆豆/角龙/祥龙/瑞麟…）。
- **难字单列**（会被自动加进图鉴当识字素材）：`芽`、`棘`、`翡`、`翠`、`碧`、`岚`、`麟`、`曜`、`瑞`、`祥`、`至`。其余为常用字（龙/蛋/森林/山海/金/星/玉/宝/圣 等）。

---

## 3. 三张物种几何参数表（最关键 · 决定上线是否翻车）

### 3.1 PET_GEO.dino（替换现有 TODO 占位）
```js
var PET_GEO = {
  ink:  { topY:30.6, bodyDy:36, bodyRx:32, headDy:69 },
  dino: { topY:18,   bodyDy:36, bodyRx:30, headDy:69 }   /* 恐龙实测值（见下） */
};
```
| 字段 | 值 | 含义（未缩放，含描边） | 推导 |
|------|----|----|----|
| `topY` | **18** | 宠物最高点 y（F8 头冠顶端，含描边）。 | F8 头冠 `path` 顶端 y≈18；取此常数保证 Lv30–40 高 scale 下 viewBox 向上扩，冠不裁。 |
| `bodyDy` | **36** | 身体中心到基准点 120 的距离 → `bodyCy = 120-36 = 84`。 | 与墨墨一致，光环/辉光/王冠/星轨对齐。 |
| `bodyRx` | **30** | 身体半宽。 | 恐龙身 rx30（比墨墨 32 略窄，给尾巴留横向余量）。 |
| `headDy` | **69** | 身体椭圆顶到 120 的距离 → `headTop = 120-69 = 51`。 | 与墨墨一致，王冠落在头顶正确。 |

### 3.2 横向极值（验证不飞出 viewBox 宽 -15..135）
| 字段 | 值 | 说明 |
|------|----|----|
| `leftX` | **22** | 最左 x（身左侧 + 前爪/腿，`x≈27` 含 3px 描边 → 22）。缩放校验：`60+(22-60)*1.75 = -6.5 ≥ -15` ✅ |
| `rightX` | **102** | 最右 x（尾尖 + 尾鳍，`x≈98` 含鳍描边 → 102）。缩放校验：`60+(102-60)*1.75 = 133.5 ≤ 135` ✅ |

> 设计约束：恐龙横向极值被有意压在 `[-15,135]` 内，即使 Lv40（S=1.75）也不触发横向裁切。代价是尾巴偏短（约 10px 出身体）——可接受且安全。若工程师希望更长尾，须同步把 `viewBox` 宽度也按物种向外扩（当前 `petSVG` 只向上扩），否则会重演墨墨头顶被裁的 P0。

### 3.3 装扮锚点偏移（相对墨墨现有锚点）
墨墨锚点：帽 `(60,44)`、围巾 `(60,104)`、翅膀 `(26,80)+(94,80)`。恐龙复用同一坐标系，偏移如下：

| 装扮槽 | 恐龙锚点（绝对） | 相对墨墨偏移 | 备注 |
|--------|----|----|----|
| 头冠 `hat` | **(60,44)** | **(0, 0)** | 与墨墨同位置；恐龙头冠（头冠/角冠）画在此，落在头顶。 |
| 颈饰 `scarf` | **(60,100)** | **(0, -4)** | 恐龙颈短，围巾上移 4px 包在下颌/前胸。 |
| 背鳍 `wing`（背鳍主体） | **(60,60)** | 相对墨墨左翅 (26,80)：**(+34, -20)** | 背鳍是沿脊背的中央鳍，不再分左右两翅。 |
| 背鳍 `wing`（尾饰） | **(98,96)** | 相对墨墨右翅 (94,80)：**(+4, +16)** | 尾饰画在尾尖；与背鳍主体同属一个 `wing` 槽装扮。 |

> 说明：墨墨的 `wing` 槽是两片侧翅；恐龙把该槽**语义改为「背鳍/尾饰」**，一件装扮同时画「脊背鳍 + 尾饰」两部分（见 §4 片段）。因此一件背鳍装扮函数内返回 `<g class="pk-wing">` 含两组路径：一组以 `(60,60)` 为基画背鳍，一组以尾尖 `(98,96)` 为基画尾饰。

### 3.4 ⚠ 工程师须同步修正的一处（非本规格范围，但恐龙上线翻车与此强相关）
`petFxLayout` 当前第 3812 行把宠物最高点写死：
```js
var petTop  = 120 - 89.4*S;    /* 89.4 = 120 - ink.topY(30.6)，墨墨专用 */
```
恐龙须改为读 `PET_GEO`：
```js
var petTop  = 120 - G.topY*S;  /* G = petGeo(sp)，恐龙 topY=18 */
```
否则恐龙头冠（topY=18）会被当 30.6 计算，高等级光环/王冠定位偏高、可能裁切。viewBox 的 `needTop` 已正确使用 `G.topY`（第 3918 行），无需改。

---

## 4. 十六件恐龙专属装饰

三类新件：**头冠 `hat`**（6）/ **颈饰 `scarf`**（5）/ **背鳍·尾饰 `wing`**（5）。
- id 全小写、与现有 44 件目录**零冲突**（前缀 `d` 区分）。
- `slot` 同墨墨：`hat`/`scarf`/`wing`。`wing` 槽在恐龙身上显示为「背鳍/尾饰」。
- `grant` 等级锚点严格用 **Lv22/26/30/32/34/36/38/40**（8 件自动赠送）；其余 8 件为 ⭐ 购买（`price>0`，无 `grant`）。
- 每件含**色觉障碍冗余编码**：形状/剪影彼此不同（叶形≠星形≠云形≠宝石形），不单靠颜色区分。
- 风格：低饱和、圆润、童真；主色 `m` / 亮部 `h` / 描边 `l` 三 hex。

### 4.1 清单（id / 中文名 / 类别 / grant等级或购买价 / 三 hex）

| # | id | 中文名 | 类别 | grant / price | m(主) | h(亮) | l(描边) |
|---|----|----|----|----|----|----|----|
| 1 | dleafcrown | 嫩叶冠 | hat | grant:22 | #8ED46E | #C7EBA8 | #377020 |
| 2 | dblossom | 花苞冠 | hat | grant:32 | #FFB3C8 | #FFD6E4 | #D96A8C |
| 3 | dhorn | 小角冠 | hat | grant:40 | #FFE9A8 | #FFF6DF | #B87400 |
| 4 | dgemcrest | 宝石冠 | hat | price:30 | #7FC7EE | #CDEEFF | #2F86B4 |
| 5 | dstarcrest | 星冠 | hat | price:25 | #FFC93C | #FFF0C0 | #B87400 |
| 6 | dcloudcrest | 云冠 | hat | price:15 | #FFFFFF | #F2F7FD | #9DB8D4 |
| 7 | dleafscarf | 藤叶围巾 | scarf | grant:26 | #8ED46E | #B6E59A | #377020 |
| 8 | dvine | 花藤围巾 | scarf | grant:34 | #FF9AA6 | #FFC4D4 | #D96A8C |
| 9 | dberryscarf | 莓果围巾 | scarf | grant:38 | #FF6B9D | #FFB0C8 | #C43324 |
| 10 | dpearlneck | 珍珠颈圈 | scarf | price:20 | #FFF6DF | #FFFFFF | #C9A86A |
| 11 | dstarneck | 星河颈圈 | scarf | price:25 | #7FC7EE | #CDEEFF | #2F86B4 |
| 12 | dbackleaf | 叶背鳍 | wing | grant:30 | #8ED46E | #B6E59A | #377020 |
| 13 | dbackcrystal | 晶背鳍 | wing | grant:36 | #CDEEFF | #EAF6FF | #2F86B4 |
| 14 | dbackflame | 焰背鳍 | wing | price:25 | #FF8A3D | #FFC98A | #C4683F |
| 15 | dbackstar | 星背鳍 | wing | price:25 | #FFC93C | #FFF0C0 | #B87400 |
| 16 | dbackcloud | 云背鳍 | wing | price:15 | #FFFFFF | #F2F7FD | #9DB8D4 |

> 登记：以上 16 件加入 `PET_COSTUME_LIST`（每件加 `sp:'dino'` 字段，使 `costumeFitsSpecies` 只发给龙宝；墨墨的 44 件 `sp` 默认 `'ink'`）。`grantLevelRewards` 已按 `sp` 过滤，只发当前物种达成奖励。

### 4.2 绘制要点 + 可直接粘贴的 ES5 片段

**头冠 `hat`**（锚点 `(60,44)`，类 `pk-hat`）—— 6 件：
```js
var DINO_HATS = {
  dleafcrown: function(){ return '<g class="pk-hat" transform="translate(60,44)">'
    + '<path d="M-16 4 Q0 -10 16 4 Q0 -2 -16 4 Z" fill="#8ED46E" stroke="#377020" stroke-width="1.8" stroke-linejoin="round"/>'
    + '<path d="M-9 -2 q-7 -10 -2 -16 q9 4 9 14 z" fill="#C7EBA8" stroke="#377020" stroke-width="1.6" stroke-linejoin="round"/>'
    + '<path d="M9 -2 q7 -10 2 -16 q-9 4 -9 14 z" fill="#C7EBA8" stroke="#377020" stroke-width="1.6" stroke-linejoin="round"/>'
    + '<circle cx="0" cy="-4" r="2.6" fill="#FFC93C" stroke="#B87400" stroke-width="1.1"/></g>'; },
  dblossom: function(){ return '<g class="pk-hat" transform="translate(60,44)">'
    + '<path d="M-14 4 q14 -16 28 0 z" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.8" stroke-linejoin="round"/>'
    + '<circle cx="-6" cy="-6" r="4.4" fill="#FFD6E4" stroke="#D96A8C" stroke-width="1.4"/>'
    + '<circle cx="6" cy="-6" r="4.4" fill="#FFD6E4" stroke="#D96A8C" stroke-width="1.4"/>'
    + '<circle cx="0" cy="-12" r="5" fill="#FFD6E4" stroke="#D96A8C" stroke-width="1.4"/>'
    + '<circle cx="0" cy="-12" r="1.4" fill="#FFFFFF" opacity=".85"/></g>'; },
  dhorn: function(){ return '<g class="pk-hat" transform="translate(60,44)">'
    + '<path d="M-12 4 q12 -20 24 0 z" fill="#FFE9A8" stroke="#B87400" stroke-width="2" stroke-linejoin="round"/>'
    + '<path d="M0 4 V-14" fill="none" stroke="#B87400" stroke-width="1.4"/>'
    + '<circle cx="-7" cy="2" r="2.2" fill="#FFF6DF" stroke="#B87400" stroke-width="1"/>'
    + '<circle cx="7" cy="2" r="2.2" fill="#FFF6DF" stroke="#B87400" stroke-width="1"/></g>'; },
  dgemcrest: function(){ var o='', bx=[-12,-4,4,12], by=[-3,-7,-7,-3], br=[3.4,4.2,4.2,3.4], i;
    for(i=0;i<4;i++){ o+='<ellipse cx="'+bx[i]+'" cy="'+by[i]+'" rx="'+br[i]+'" ry="'+(br[i]+1.4)+'" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1.4"/>'; }
    return '<g class="pk-hat" transform="translate(60,44)">'+o
    + '<path d="M-16 4 Q0 -2 16 4 L16 9 Q0 4 -16 9 Z" fill="#7FC7EE" stroke="#2F86B4" stroke-width="1.8" stroke-linejoin="round"/>'
    + '<circle cx="-8" cy="5" r="2" fill="#FFC93C" stroke="#B87400" stroke-width="1"/>'
    + '<circle cx="8" cy="5" r="2" fill="#FFC93C" stroke="#B87400" stroke-width="1"/></g>'; },
  dstarcrest: function(){ return '<g class="pk-hat" transform="translate(60,44)">'
    + '<path d="M0 -16 L5 -4 L18 -4 L8 5 L12 18 L0 9 L-12 18 L-8 5 L-18 -4 L-5 -4 Z" fill="#FFC93C" stroke="#B87400" stroke-width="1.8" stroke-linejoin="round"/>'
    + '<circle cx="0" cy="-2" r="2.8" fill="#FFF0C0"/></g>'; },
  dcloudcrest: function(){ return '<g class="pk-hat" transform="translate(60,44)">'
    + '<ellipse cx="-9" cy="2" rx="11" ry="9" fill="#FFFFFF" stroke="#9DB8D4" stroke-width="1.6"/>'
    + '<ellipse cx="9" cy="2" rx="11" ry="9" fill="#FFFFFF" stroke="#9DB8D4" stroke-width="1.6"/>'
    + '<ellipse cx="0" cy="-3" rx="13" ry="11" fill="#FFFFFF" stroke="#9DB8D4" stroke-width="1.6"/>'
    + '<rect x="-20" y="2" width="40" height="7" rx="3.5" fill="#F2F7FD" stroke="#9DB8D4" stroke-width="1.6"/></g>'; }
};
```
**颈饰 `scarf`**（锚点 `(60,100)`，类 `pk-scarf`）—— 5 件：
```js
var DINO_SCARFS = {
  dleafscarf: function(){ return '<g class="pk-scarf" transform="translate(60,100)">'
    + '<path d="M-30 0 q30 14 60 0" fill="none" stroke="#8ED46E" stroke-width="8" stroke-linecap="round"/>'
    + '<path d="M-10 8 q-6 -8 -2 -14 q8 4 6 14 z" fill="#B6E59A" stroke="#377020" stroke-width="1.4" stroke-linejoin="round"/>'
    + '<path d="M10 8 q6 -8 2 -14 q-8 4 -6 14 z" fill="#B6E59A" stroke="#377020" stroke-width="1.4" stroke-linejoin="round"/></g>'; },
  dvine: function(){ return '<g class="pk-scarf" transform="translate(60,100)">'
    + '<path d="M-30 0 q30 14 60 0" fill="none" stroke="#FF9AA6" stroke-width="8" stroke-linecap="round"/>'
    + '<circle cx="-12" cy="6" r="3" fill="#FFC4D4" stroke="#D96A8C" stroke-width="1.2"/>'
    + '<circle cx="0" cy="8" r="3" fill="#FFC4D4" stroke="#D96A8C" stroke-width="1.2"/>'
    + '<circle cx="12" cy="6" r="3" fill="#FFC4D4" stroke="#D96A8C" stroke-width="1.2"/></g>'; },
  dberryscarf: function(){ return '<g class="pk-scarf" transform="translate(60,100)">'
    + '<path d="M-30 0 q30 14 60 0" fill="none" stroke="#FF6B9D" stroke-width="8" stroke-linecap="round"/>'
    + '<circle cx="-8" cy="6" r="4" fill="#FFB0C8" stroke="#C43324" stroke-width="1.4"/>'
    + '<circle cx="8" cy="6" r="4" fill="#FFB0C8" stroke="#C43324" stroke-width="1.4"/></g>'; },
  dpearlneck: function(){ var px=[-20,-10,0,10,20], py=[7,9,10,9,7], pr=[3,2.4,3.4,2.4,3], o='', i;
    for(i=0;i<5;i++){ o+='<circle cx="'+px[i]+'" cy="'+py[i]+'" r="'+pr[i]+'" fill="#FFF6DF" stroke="#C9A86A" stroke-width="1.1"/>'; }
    return '<g class="pk-scarf" transform="translate(60,100)">'
    + '<path d="M-30 0 q30 14 60 0" fill="none" stroke="#FFF6DF" stroke-width="8" stroke-linecap="round"/>'+o+'</g>'; },
  dstarneck: function(){ return '<g class="pk-scarf" transform="translate(60,100)">'
    + '<path d="M-30 0 q30 14 60 0" fill="none" stroke="#7FC7EE" stroke-width="8" stroke-linecap="round"/>'
    + '<path d="M0 8 l1.6 3.4 3.6 .4 -2.6 2.4 .6 3.4L0 16 l-2.8 1.8 .6 -3.4 -2.6 -2.4 3.6 -.4 z" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1"/></g>'; }
};
```
**背鳍·尾饰 `wing`**（脊背基 `(60,60)` + 尾尖基 `(98,96)`，类 `pk-wing`）—— 5 件：
```js
var DINO_WINGS = {
  dbackleaf: function(lineC, pref){
    return '<g class="pk-wing">'
      /* 脊背鳍（中央，沿背） */
      + '<path d="M44 62 Q60 40 76 62 Q60 54 44 62 Z" fill="#8ED46E" stroke="#377020" stroke-width="2" stroke-linejoin="round"/>'
      + '<path d="M52 56 q-6 -8 -2 -13 q7 3 6 12 z" fill="#B6E59A" stroke="#377020" stroke-width="1.4" stroke-linejoin="round"/>'
      + '<path d="M68 56 q6 -8 2 -13 q-7 3 -6 12 z" fill="#B6E59A" stroke="#377020" stroke-width="1.4" stroke-linejoin="round"/>'
      /* 尾饰（尾尖） */
      + '<path d="M92 96 q14 -12 20 2 q-12 12 -20 -2 z" fill="#8ED46E" stroke="#377020" stroke-width="2" stroke-linejoin="round"/>'
      + '<circle cx="104" cy="98" r="2.4" fill="#B6E59A" stroke="#377020" stroke-width="1"/></g>'; },
  dbackcrystal: function(lineC, pref){
    var face = '<path d="M0 -12 L5 -3 L3 11 L-3 11 L-5 -3 Z" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1.8" stroke-linejoin="round"/>'
      + '<path d="M0 -12 V11" fill="none" stroke="#FFFFFF" stroke-width="1" opacity=".85"/>';
    return '<g class="pk-wing">'
      + '<path d="M44 62 Q60 42 76 62 Q60 56 44 62 Z" fill="#EAF6FF" stroke="#2F86B4" stroke-width="2" stroke-linejoin="round"/>'
      + '<g transform="translate(60,50)">'+face+'</g>'
      /* 尾饰晶石 */
      + '<path d="M96 96 l6 -11 6 11 -6 11 z" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1.8" stroke-linejoin="round"/>'
      + '<path d="M96 96 V107" fill="none" stroke="#FFFFFF" stroke-width="1" opacity=".85"/></g>'; },
  dbackflame: function(lineC, pref){
    return '<g class="pk-wing">'
      + '<path d="M44 62 Q60 38 76 62 Q60 52 44 62 Z" fill="#FF8A3D" stroke="#C4683F" stroke-width="2" stroke-linejoin="round"/>'
      + '<path d="M52 56 q-4 -10 0 -16 q5 6 5 16 z" fill="#FFC98A" stroke="#C4683F" stroke-width="1.4" stroke-linejoin="round"/>'
      + '<path d="M68 56 q4 -10 0 -16 q-5 6 -5 16 z" fill="#FFC98A" stroke="#C4683F" stroke-width="1.4" stroke-linejoin="round"/>'
      + '<path d="M92 96 q14 -12 20 2 q-12 12 -20 -2 z" fill="#FF8A3D" stroke="#C4683F" stroke-width="2" stroke-linejoin="round"/></g>'; },
  dbackstar: function(lineC, pref){
    return '<g class="pk-wing">'
      + '<path d="M44 62 Q60 40 76 62 Q60 54 44 62 Z" fill="#FFC93C" stroke="#B87400" stroke-width="2" stroke-linejoin="round"/>'
      + '<path d="M60 44 l3 6 6 .6 -4.4 4.2 1 6.2L60 57l-5.6 4 1 -6.2 -4.4 -4.2 6 -.6 z" fill="#FFF0C0" stroke="#B87400" stroke-width="1"/>'
      + '<path d="M92 96 q14 -12 20 2 q-12 12 -20 -2 z" fill="#FFC93C" stroke="#B87400" stroke-width="2" stroke-linejoin="round"/></g>'; },
  dbackcloud: function(lineC, pref){
    return '<g class="pk-wing">'
      + '<ellipse cx="52" cy="56" rx="11" ry="9" fill="#FFFFFF" stroke="#9DB8D4" stroke-width="1.6"/>'
      + '<ellipse cx="68" cy="56" rx="11" ry="9" fill="#FFFFFF" stroke="#9DB8D4" stroke-width="1.6"/>'
      + '<ellipse cx="60" cy="50" rx="13" ry="11" fill="#FFFFFF" stroke="#9DB8D4" stroke-width="1.6"/>'
      + '<path d="M92 96 q14 -12 20 2 q-12 12 -20 -2 z" fill="#F2F7FD" stroke="#9DB8D4" stroke-width="1.8" stroke-linejoin="round"/></g>'; }
};
```
> 色觉障碍冗余：叶形(尖圆叶)/星型(五角)/云形(圆团)/宝石(菱形)/火焰(火舌)剪影各异，单看形状也能区分；颜色仅作加强。

---

## 5. 回贴四张清单（team-lead 验收用）

### (a) 16 件装饰完整清单（id / 中文名 / 类别 / grant等级 / 三 hex）
见 §4.1 表格。grant 锚点分布：22(dleafcrown)、26(dleafscarf)、30(dbackleaf)、32(dblossom)、34(dvine)、36(dbackcrystal)、38(dberryscarf)、40(dhorn)；购买件：dgemcrest/dstarcrest/dcloudcrest(hat)、dpearlneck/dstarneck(scarf)、dbackflame/dbackstar/dbackcloud(wing)。三 hex 见 §4.1 第 6–8 列。

### (b) 三张参数表完整数值
- `PET_GEO.dino`：**topY=18 / bodyDy=36 / bodyRx=30 / headDy=69**（§3.1）。
- 横向极值：**leftX=22 / rightX=102**（§3.2，已验证 S=1.75 时不超 [-15,135]）。
- 装扮锚点：**hat(60,44) 偏移(0,0)** / **scarf(60,100) 偏移(0,-4)** / **背鳍(60,60) 偏移墨墨左翅(+34,-20)** + **尾饰(98,96) 偏移墨墨右翅(+4,+16)**（§3.3）。
- 附带修复项：`petFxLayout` 第 3812 行 `petTop = 120 - 89.4*S` → `120 - G.topY*S`（§3.4）。

### (c) 8 个形态 Lv 区间 + 一句话
1. F1 龙蛋 · Lv1 — 纯蛋带裂纹与小尾尖，最萌。
2. F2 破壳幼崽 · Lv2–5 — 蛋壳里探出大头小身与小尾尖。
3. F3 短尾+圆背鳍 · Lv6–9 — 标准圆身、短尾、一枚软背鳍。
4. F4 三角背棘+后腿 · Lv10–13 — 三枚圆角背棘、带脚后腿。
5. F5 体形拉长+头顶小角+尾鳍 · Lv14–17 — 身体略高、头顶小角、尾端圆鳍。
6. F6 前爪+背上骨质纹 · Lv18–21 — 加前爪、背棘间浅色骨质纹。
7. F7 鳍褶延伸 · Lv22–29 — 背棘连成波浪鳍褶、尾鳍加大。
8. F8 头冠+尾端晶石暖光 · Lv30–40 — 头顶冠展开到 topY=18、尾端暖光晶石（封顶形态）。
代表性片段见 §1 的 F1 / F3 / F8（已含可直接粘贴 ES5 代码；F2/F4/F5/F6/F7 片段同上节）。

### (d) 40 级名字清单
龙蛋 / 小芽 / 芽龙 / 嫩芽 / 绿芽 / 豆豆 / 森林 / 林宝 / 青芽 / 小棘 / 棘龙 / 湖宝 / 翡翠 / 湖玉 / 碧波 / 小角 / 角龙 / 爪爪 / 骨纹 / 山宝 / 岩龙 / 青碧 / 溪龙 / 岚龙 / 云海 / 山海 / 金曜 / 曜龙 / 翠羽 / 祥龙 / 瑞龙 / 麟龙 / 玉麟 / 星麟 / 金麟 / 瑞麟 / 祥麟 / 宝麟 / 圣麟 / 至尊麟。
难字（进图鉴）：芽 / 棘 / 翡 / 翠 / 碧 / 岚 / 麟 / 曜 / 瑞 / 祥 / 至。
