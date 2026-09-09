# 宠物装扮 · Lv21–Lv40 高阶形态美术规格

> 项目：《识字大冒险》单文件 HTML（`识字大冒险.html`）
> 目标用户：5 岁女孩 ｜ 宠物：字灵「墨墨」（紫色系 Q 版小精灵）
> 产出：16 件新装扮（帽子 4 / 围巾 4 / 翅膀 4 / 背景 4），**形状级 SVG 规格**，工程师可直接照抄为 `PET_HATS` / `PET_SCARFS` / `PET_WINGS` / `petBgSVG` 分支。
> 硬约束：纯内联 SVG / CSS，零位图、零外链、零 web font。
> 本文件**不修改** `识字大冒险.html`（工程实现由他人完成）。

---

## 0. 一页速览

| # | id | 中文名 | 类别 | **发放 Lv** | 稀有度标价<br>（仅参考） | 主色 / 亮部 / 描边 |
|---|---|---|---|---|---|---|
| 1 | `aurora` | 极光冠 | hat | **22** | 30 | `#7FC7EE` / `#CDEEFF` / `#2F86B4` |
| 2 | `gemcrown` | 宝石冠 | hat | **27** | 40 | `#FFD86B` / `#FFE9A8` / `#B87400` |
| 3 | `mooncrown` | 月牙冠 | hat | **32** | 50 | `#FFD86B` / `#FFF6DF` / `#B87400` |
| 4 | `phoenix` | 凤羽冠 | hat | **37** | 60 | `#FFB79B` / `#FFDCC9` / `#C4683F` |
| 5 | `nebula` | 星云围巾 | scarf | **23** | 30 | `#7FC7EE` / `#CDEEFF` / `#2F86B4` |
| 6 | `pearl` | 珍珠围巾 | scarf | **28** | 40 | `#FFF6DF` / `#FFFFFF` / `#C9A86A` |
| 7 | `ribbon` | 缎带围巾 | scarf | **33** | 50 | `#FF6B9D` / `#FFB3C8` / `#D96A8C` |
| 8 | `cloudscarf` | 云绒围巾 | scarf | **38** | 60 | `#E8F1FB` / `#FFFFFF` / `#9DB8D4` |
| 9 | `aurorawing` | 极光翅膀 | wing | **24** | 30 | `#7FC7EE` / `#CDEEFF` / `#2F86B4` |
| 10 | `angelwing` | 天使之翼 | wing | **29** | 40 | `#FFF6DF` / `#FFFFFF` / `#C9A86A` |
| 11 | `crystalwing` | 水晶翅膀 | wing | **34** | 50 | `#CDEEFF` / `#FFFFFF` / `#2F86B4` |
| 12 | `dragonwing` | 祥龙翅膀 | wing | **39** | 60 | `#FFB79B` / `#FFDCC9` / `#C4683F` |
| 13 | `skygarden` | 云上花园 | bg | **25** | 30 | `#E4F2FB` / `#FFFFFF` / `#9DB8D4` |
| 14 | `starsea` | 星海背景 | bg | **30** | 40 | `#1E3A5E` / `#4E7BA8` / `#FFF6DF` |
| 15 | `gemcave` | 宝石洞窟 | bg | **35** | 50 | `#1F5150` / `#4E9E96` / `#173F3E` |
| 16 | `aurorabg` | 极光天幕 | bg | **40** | 60 | `#24436E` / `#6E93B4` / `#2E5A5A` |

> ### ⚠ 获取机制（主理人裁定 v1.1）
> **这 16 件不走 ⭐ 购买，改「等级达标自动获得（grant）」。**
> - 上表「稀有度标价」列**仅作稀有度排序与展示参考，不参与 ⭐ 经济**，工程实现时**不要**写进 `price` 参与扣星逻辑（可保留数字用于排序，或置 0）。
> - 「发放 Lv」= `minLv`，供 `grantLevelRewards()` 判断发放时机。
> - 这 16 件**仍必须登记进 `PET_COSTUME_LIST`**（否则 `costumeMeta(id)` 返回 null → 渲染不出来），但需带 `grant:true` 标记，**商店渲染时跳过 `grant:true` 的条目**。
> - 发放锚点：Lv22/23/24/25 · 27/28/29/30 · 32/33/34/35 · 37/38/39/40。
>   Lv21/26/31/36 不发装扮，改发零成本称号（策划侧出品）。**美术空窗最长 1 级**，密度确认无异议。

全部 id 已与现有 28 件比对，**零重复**。扩展后全目录 28 → 44 件。

---

## 1. 设计基调

### 1.1 延续什么
- 低饱和、柔和、圆润、可爱；**所有尖角一律 `stroke-linejoin="round"` / `stroke-linecap="round"`**
- 描边驱动造型（每个独立色块都带同族深色描边），不靠渐变或阴影堆体积
- 复用既有调色板（见 §2），只在必要处补 16 个同调新色

### 1.2 Lv21–Lv40 的"高阶感"怎么来（不是暗黑/酷炫）
高阶感 = **材质 + 光效 + 层叠**，不是"变凶 / 变暗"。

| 手法 | 具体做法 |
|---|---|
| 材质感 | 宝石（多边形 + 中心高光线）、珍珠（圆 + 左上高光点）、水晶（棱面 + 白色中脊） |
| 光效感 | 全部为**静态**高光（白色小圆/短线，`opacity .7–.9`），**不新增任何循环动画** |
| 层叠感 | 帽子 = 后层造型 + 前层箍 + 前层宝石，共 3 层；围巾 = 主弧 + 内弧/挂饰 2 层 |
| 神兽/天象题材 | 极光、月牙、凤羽、祥龙、星海、宝石洞 —— 童书插画式的"神兽"，Q 版圆润处理 |

### 1.3 明确的禁区
- ❌ 不出现黑、深灰、霓虹高对比
- ❌ 不出现尖锐棱角、骷髅、火焰尖刺、机械/赛博元素
- ❌ 背景不出现密集噪点/网格（5 岁孩子视觉负荷 + 与主体抢注意力）
- ❌ 新装扮**不新增循环动画**（`prefers-reduced-motion` 友好，也省性能）

---

## 2. 调色板

### 2.1 复用色（不新增字节）
`#FFB3C8` `#FF9AA6` `#FF6B9D` `#D96A8C` ｜ `#FFC93C` `#FFD86B` `#FFE9A8` `#FFF6DF` `#B87400` ｜
`#8ED46E` `#A3E081` `#377020` ｜ `#3DA5E0` `#9B6DFF` `#7C5CFF` ｜ `#FFFFFF` `#C9D6E8` `#9DB8D4` ｜
`#C9A86A` `#FBE9C8` ｜ `#E4F2FB` `#C6E7F7` `#EAF3FB` `#EFF7E6` `#BFE0A8` ｜ `#8A5A2B` `#5A3C28`

### 2.2 新增 16 色（均为低饱和柔和色，已验证与既有色同族）
| 用途 | 主色 | 亮部 | 描边 |
|---|---|---|---|
| 冰蓝（极光/星云/水晶） | `#7FC7EE` | `#CDEEFF` | `#2F86B4` |
| 蜜桃（凤羽/祥龙） | `#FFB79B` | `#FFDCC9` | `#C4683F` |
| 云白蓝（云绒围巾） | `#E8F1FB` | `#FFFFFF` | `#9DB8D4` |
| 夜海（星海背景） | `#1E3A5E` | `#4E7BA8` | `#2C5580` |
| 洞青（宝石洞窟） | `#1F5150` | `#4E9E96` | `#173F3E`（＋钟乳石 `#2A6B68`） |
| 暮天（极光天幕） | `#24436E` | `#6E93B4` | `#2E5A5A`（雪松） |

> 字节预算：16 色 × 7 字符 ≈ **112 B** 声明 + 引用，全量落地后增量 < 6 KB（含 16 件绘制代码），对 385 KB 单文件无影响。

### 2.3 宠物主色参考（用于对比校验）
- Lv3–Lv9：`#9B6DFF → #220D82`（中深紫）
- Lv10–Lv14：`#1A0A66 → #4A3CE0`（深紫蓝）
- **Lv15–Lv20：`#6A5CFF → #BCADFF`（中浅 → 极浅薰衣草）** ← 见 §6 风险 R2
- 描边 Lv5+ 恒为金 `#FFC93C` / `#FFD86B` / `#FFE9A8` / `#FFF2C0`

---

## 3. 坐标系与工程约束（**实现前必读**）

### 3.1 宠物 SVG 坐标系
```
<svg class="pet" viewBox="-15 11 150 150">      → 可见区 x ∈ [-15,135], y ∈ [11,161]
内容围绕 x=60 居中；整包在 <g class="pk-all" transform="translate(60,120) scale(S) translate(-60,-120)">
```

| 锚点 | 坐标 | 说明 |
|---|---|---|
| 帽子 | `translate(60,44)` | 头顶。头顶轮廓 y=51 |
| 围巾 | `translate(60,104)` | 下巴/胸腹。肚皮 cy=94 ry=17（底 111），身体底 117 |
| 翅膀 | 绝对坐标，绕 `(26,80)` / `(94,80)` 各 `rotate(∓18~22)` | **在身体之前绘制 → 永远在身体背后** |
| 地面阴影 | `ellipse cx=60 cy=124 rx=30 ry=6` | 围巾挂饰不要探到 y>124 |
| Lv4+ 光环 | `ellipse cx=60 cy=30 rx=24 ry=7.5`（金色虚线） | 头顶上方 |
| Lv5+ 王冠 | `translate(60,42)`，占 **y 29–50，x ±14** | ⚠ 与帽区完全重叠 |
| Lv5+ 环绕字灵 | 5 颗 r=7，`translate(60,28)`，绕 `(60,86)` 半径 58 旋转 | ⚠ 扫过 **y 21–35** |

### 3.2 帽子安全盒（**硬约束，四顶新帽全部遵守**）
```
局部坐标（相对 translate(60,44)）：
  x ∈ [-26, +26]         → 绝对 x ∈ [34, 86]
  峰顶 y ≥ -12           → 绝对 y ≥ 32
  箍下沿 y ≤ +10         → 绝对 y ≤ 54
```
**为什么"宽而扁"而不是"高而尖"**：Lv21+ 的玩家 100% 处于 Lv5+ 形态，头顶上方 y∈[21,35] 被光环 + 环绕字灵占满；任何高耸造型都会被轨道字灵反复穿过，视觉噪声极大。所以高阶帽走**冠冕/额饰**路线。

### 3.3 围巾安全盒
```
局部坐标（相对 translate(60,104)）：
  x ∈ [-30, +30]         → 绝对 x ∈ [30, 90]
  y ∈ [ -8, +20]         → 绝对 y ∈ [96, 124]（下沿不得超过阴影 124）
```

### 3.4 翅膀安全盒
```
绝对坐标：左侧 x ∈ [4, 30]，右侧 x ∈ [90, 116]，y ∈ [50, 110]
（身体 x∈[28,92] 会遮住内侧，造型要往外探）
```

### 3.5 背景"主体框"规则（**四张新背景全部遵守**）
背景是独立 `<svg viewBox="0 0 200 200" preserveAspectRatio="xMidYMid slice">`。宠物在 82% 舞台上居中，换算到背景坐标：

| 部位 | 背景坐标 |
|---|---|
| 身体 | 中心 (100, 98)，半径 rx≈35 / ry≈36 → **x∈[65,135], y∈[62,134]** |
| 含帽子/翅膀整体 | **x∈[34,166], y∈[41,148]** |

> **规则**：装饰元素一律**不得进入主体框 `x∈[60,140] / y∈[55,145]`**。
> 框内只允许：① 渐变底 ② 柔光圆 ③ opacity ≤ .42 的大面积极光带。
> 装饰区 = 四角、上边 y<50、下边 y>150、左右边 x<56 或 x>144。

### 3.6 背景"柔光板"（**强制，每张背景都必须有**）
在 `(100,104)` 放一枚 `rx≈60 / ry≈64` 的径向渐变椭圆，把宠物从背景里"托"出来。**这一招不依赖色相，是色觉障碍安全的对比保障**：

- 浅色背景 → 柔光板用**深色**：`fill="#5A3C28" opacity=".10"`
- 深色背景 → 柔光板用**浅色**：`fill="#FFFFFF" opacity=".16–.22"`

### 3.7 ⚠ 发现的三处结构冲突（需工程侧确认，见 §6）
1. **R1 缩放裁切**：`scale` 已从 Lv1 的 .75 涨到 Lv20 的 **1.75**。按 `y' = 120 + (y-120)·S`，S=1.75 时头顶 y=51 → **y' = −0.75，已跑出 viewBox 上沿 11**。宠物页靠 `.pet-slot .pet{overflow:visible}` 撑住；但**商店卡片 `.pc-prev{overflow:hidden}` 且 `.pc-prev .pet` 没有 `overflow:visible` → 卡片预览会被裁头**。
2. **R2 主体变白**：Lv15–Lv20 的 `theme` 从 `#6A5CFF` 一路变浅到 `#BCADFF`，Lv20 肚皮已是 `#FFFFFF`。若 Lv21–40 继续变浅，宠物会变成"白团子"，**任何背景都压不住**。
3. **R3 等级门禁缺失**：`costumeUnlockLv(slot)` 只按槽位返回 `wing:3 / hat·scarf:2 / bg:1`，**没有 per-item 等级门槛** → 新增的 Lv21–40 装扮在 Lv2 就能买能戴，"Lv21–40"名存实亡。

---

## 4. 装扮逐件规格

> 尺寸换算基准：显示 120 px → **0.80 px/单位**；200 px → **1.33 px/单位**；商店卡 84 px → **0.56 px/单位**。
> 通用下限：**stroke-width ≥ 1.4 单位**（84 px → 0.78 px 仍可见）；**独立图形直径 ≥ 4.4 单位**（84 px → 2.5 px）；**同类重复元素中心间距 ≥ 6 单位**。

---

### 4.1 帽子

#### ① `aurora` 极光冠 ｜ Lv22 ｜ 30⭐ ｜ myth

**配色**：主 `#7FC7EE` / 亮 `#CDEEFF` / 描边 `#2F86B4`；辅 `#8ED46E`（中羽）、`#FFC93C`、`#FF6B9D`（宝石）

**绘制要点**（局部坐标，原点 = 绝对 (60,44)）
1. **后层三片极光飘带**（先画，被箍压住根部）
   - 左飘带：叶形 `M-13 3 C -16 -4 -13 -8 -9 -7 C -8 -3 -9 0 -13 3 Z`，峰顶 y=−7
   - 右飘带：镜像 `M13 3 C 16 -4 13 -8 9 -7 C 8 -3 9 0 13 3 Z`
   - 中飘带（最高最宽）：`M0 3 C -5 -4 -4 -11 0 -12 C 4 -11 5 -4 0 3 Z`，峰顶 y=−12
2. **弧箍**（叠在飘带之上）：`M-22 0 Q0 -8 22 0 L22 6 Q0 -2 -22 6 Z` —— 上沿两端 y=0、中间 y=−8；下沿两端 y=6、中间 y=−2；箍厚 6。弧度**中间高两端低**，贴合圆头顶（头顶 y=51）
3. **三颗宝石**（嵌在箍中线上，中线 y 值：x=±11 处 y=0，x=0 处 y=−1）
   - 左：`circle r=2.4` 金
   - 中：`rect 6.4×6.4 rx=1` 绕 `(0,-1)` 旋转 45° → **菱形**，粉
   - 右：`circle r=2.4` 金

**尺寸约束**：整体 52×20 单位；在 200 px 下约占 **69×27 px**，84 px 卡下约 **29×11 px**。三颗宝石直径 4.8 / 6.4 单位 → 84 px 下 2.7 / 3.6 px，可辨。中飘带比侧飘带高 5 单位 → 200 px 下差 6.7 px，明显。

**色觉可访问性**：区分不靠颜色 —— 三片飘带靠**高度**（中 > 侧），三颗宝石靠**形状**（圆 / 菱 / 圆）+ **大小**（中最大）。

```js
/* PET_HATS */
aurora: function(){
  return '<g class="pk-hat" transform="translate(60,44)">'+
    '<path d="M-13 3 C -16 -4 -13 -8 -9 -7 C -8 -3 -9 0 -13 3 Z" fill="#7FC7EE" stroke="#2F86B4" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M13 3 C 16 -4 13 -8 9 -7 C 8 -3 9 0 13 3 Z" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M0 3 C -5 -4 -4 -11 0 -12 C 4 -11 5 -4 0 3 Z" fill="#8ED46E" stroke="#377020" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M-22 0 Q0 -8 22 0 L22 6 Q0 -2 -22 6 Z" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1.8" stroke-linejoin="round"/>'+
    '<circle cx="-11" cy="0" r="2.4" fill="#FFC93C" stroke="#B87400" stroke-width="1.1"/>'+
    '<rect x="-3.2" y="-4.2" width="6.4" height="6.4" rx="1" transform="rotate(45 0 -1)" fill="#FF6B9D" stroke="#D96A8C" stroke-width="1.1"/>'+
    '<circle cx="11" cy="0" r="2.4" fill="#FFC93C" stroke="#B87400" stroke-width="1.1"/>'+
  '</g>';
}
```

---

#### ② `gemcrown` 宝石冠 ｜ Lv27 ｜ 40⭐ ｜ myth

**配色**：主 `#FFD86B` / 亮 `#FFE9A8` / 描边 `#B87400`；宝石 `#7FC7EE` `#FF6B9D` `#8ED46E`

**绘制要点**
1. **5 个圆头冠尖**（`<ellipse>`，先画，被箍压住底部）—— 位置/半径/高度递增：
   | x | cy | rx | ry | 露出箍上的高度 |
   |---|---|---|---|---|
   | ±18 | −1 | 3.6 | 4.8 | 5.6 |
   | ±9 | −4 | 4.2 | 5.6 | 8.1 |
   | 0 | −6 | 4.8 | 6.4 | 10.0 |
   峰顶最高 y=−12.4（绝对 31.6）
2. **弧箍**：`M-22 4 Q0 -2 22 4 L22 10 Q0 4 -22 10 Z`（比 `aurora` 低 4 单位，给冠尖让位）
3. **5 颗宝石**嵌箍中线（中线 y：x=±18→6.0，x=±9→4.5，x=0→4.0），形状**圆 / 菱 / 圆(大) / 菱 / 圆**交替，中央那颗最大（r=2.8）

**与现有 `crown` 的区别**：`crown` 是**尖三角**锯齿；`gemcrown` 是**圆头椭圆**冠尖 + 五色宝石 + 更宽的箍（±22 vs ±14），明显更高阶。

**尺寸约束**：整体 52×24 单位；200 px 下约 **69×32 px**。冠尖露出高度 5.6 / 8.1 / 10.0 单位 → 200 px 下 7.5 / 10.8 / 13.3 px，三级梯度清晰。

**色觉可访问性**：冠尖靠**高度三级差**；宝石靠**形状**（圆/菱）+ **中央最大**。

```js
gemcrown: function(){
  var o='', bx=[-18,-9,0,9,18], by=[-1,-4,-6,-4,-1], br=[3.6,4.2,4.8,4.2,3.6], bh=[4.8,5.6,6.4,5.6,4.8], i;
  for(i=0;i<5;i++){
    o += '<ellipse cx="'+bx[i]+'" cy="'+by[i]+'" rx="'+br[i]+'" ry="'+bh[i]+'" fill="#FFE9A8" stroke="#B87400" stroke-width="1.4"/>';
  }
  return '<g class="pk-hat" transform="translate(60,44)">'+o+
    '<path d="M-22 4 Q0 -2 22 4 L22 10 Q0 4 -22 10 Z" fill="#FFD86B" stroke="#B87400" stroke-width="1.8" stroke-linejoin="round"/>'+
    '<circle cx="-18" cy="6" r="2.2" fill="#7FC7EE" stroke="#2F86B4" stroke-width="1"/>'+
    '<rect x="-11.3" y="2.2" width="4.6" height="4.6" rx=".8" transform="rotate(45 -9 4.5)" fill="#FF6B9D" stroke="#D96A8C" stroke-width="1"/>'+
    '<circle cx="0" cy="4" r="2.8" fill="#8ED46E" stroke="#377020" stroke-width="1"/>'+
    '<rect x="6.7" y="2.2" width="4.6" height="4.6" rx=".8" transform="rotate(45 9 4.5)" fill="#FF6B9D" stroke="#D96A8C" stroke-width="1"/>'+
    '<circle cx="18" cy="6" r="2.2" fill="#7FC7EE" stroke="#2F86B4" stroke-width="1"/>'+
  '</g>';
}
```

---

#### ③ `mooncrown` 月牙冠 ｜ Lv32 ｜ 50⭐ ｜ myth

**配色**：主 `#FFD86B` / 亮 `#FFF6DF` / 描边 `#B87400`

**绘制要点**
1. **细金箍**（最底层，衬托月牙）：`M-24 4 Q0 -2 24 4 L24 9 Q0 3 -24 9 Z`，厚 5
2. **卧月牙**（牛角朝上、开口朝上）—— 两段三次贝塞尔闭合：
   - 外缘（下凸）：`M-12 -5 C -12 10 12 10 12 -5`
   - 内缘（浅凹）：`C 6 -1 -6 -1 -12 -5`
   - 合起来：`M-12 -5 C -12 10 12 10 12 -5 C 6 -1 -6 -1 -12 -5 Z`
   - 结果：两端牛角在 `(±12,-5)`（绝对 y=39），腹部最低 y≈+5（绝对 49），最厚处 ≈8 单位
3. **3 颗四角星**（`sparkle` 路径 `M0 -4 Q.8 -1 4 0 Q.8 1 0 4 Q-.8 1 -4 0 Q-.8 -1 0 -4 Z`）
   - 大星：`translate(0,-10) scale(.85)` → 顶 y=−13.4（绝对 30.6），fill `#FFF6DF`
   - 小星 ×2：`translate(∓17,-3) scale(.6)`

**尺寸约束**：整体 48×24 单位；月牙厚 8 单位 → 84 px 卡下 4.5 px，可辨；小星直径 4.8 单位 → 84 px 下 2.7 px（勉强，故大星放大到 6.8 单位做视觉锚点）。

**色觉可访问性**：月牙是**唯一的大块 C 形**（靠剪影识别，不依赖颜色）；三颗星靠**大小**（中 > 侧）。

```js
mooncrown: function(){
  function spark(x,y,s){
    return '<path transform="translate('+x+','+y+') scale('+s+')" '+
      'd="M0 -4 Q.8 -1 4 0 Q.8 1 0 4 Q-.8 1 -4 0 Q-.8 -1 0 -4 Z" '+
      'fill="#FFF6DF" stroke="#B87400" stroke-width="1.1" stroke-linejoin="round"/>';
  }
  return '<g class="pk-hat" transform="translate(60,44)">'+
    '<path d="M-24 4 Q0 -2 24 4 L24 9 Q0 3 -24 9 Z" fill="#FFE9A8" stroke="#B87400" stroke-width="1.6" stroke-linejoin="round"/>'+
    '<path d="M-12 -5 C -12 10 12 10 12 -5 C 6 -1 -6 -1 -12 -5 Z" fill="#FFD86B" stroke="#B87400" stroke-width="1.6" stroke-linejoin="round"/>'+
    spark(0,-10,.85)+spark(-17,-3,.6)+spark(17,-3,.6)+
  '</g>';
}
```

---

#### ④ `phoenix` 凤羽冠 ｜ Lv37 ｜ 60⭐ ｜ myth

**配色**：主 `#FFB79B` / 亮 `#FFDCC9` / 描边 `#C4683F`；箍 `#FFD86B` / `#B87400`；羽轴与鳞 `#FFC93C`

**绘制要点**
1. **中央大羽**（最高最宽）：`M0 5 C -7 -3 -5 -11 0 -12 C 5 -11 7 -3 0 5 Z`，峰顶 y=−12（绝对 32），宽 14，加一条羽轴 `M0 4 V-9`（`#C4683F` w=1）
2. **内侧羽 ×2**：左 `M-9 4 C -15 -2 -14 -9 -9 -10 C -5 -8 -5 -1 -9 4 Z`；右为 x 取反镜像。峰顶 y=−10
3. **外侧小羽 ×2**：左 `M-17 3 C -22 -1 -21 -6 -17 -7 C -14 -6 -14 0 -17 3 Z`；右镜像。峰顶 y=−7
4. **珠箍**：`M-22 4 Q0 -2 22 4 L22 10 Q0 4 -22 10 Z` + 3 颗 `circle r=2.2` 在 `(∓12,5.8)` 与 `(0,4)`

**尺寸约束**：整体 44×22 单位。**三级羽尺寸梯度**（宽/高：14×17 / 12×14 / 10×10）→ 200 px 下 18.7×22.7 / 16×18.7 / 13.3×13.3 px，差异清晰；84 px 下最小一片仍有 5.6×5.6 px。

**色觉可访问性**：三片羽靠**尺寸 + 位置**（中 / 内 / 外），不靠颜色；羽轴线条提供额外形状线索。

```js
phoenix: function(){
  return '<g class="pk-hat" transform="translate(60,44)">'+
    '<path d="M-17 3 C -22 -1 -21 -6 -17 -7 C -14 -6 -14 0 -17 3 Z" fill="#FFDCC9" stroke="#C4683F" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M17 3 C 22 -1 21 -6 17 -7 C 14 -6 14 0 17 3 Z" fill="#FFDCC9" stroke="#C4683F" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M-9 4 C -15 -2 -14 -9 -9 -10 C -5 -8 -5 -1 -9 4 Z" fill="#FFB79B" stroke="#C4683F" stroke-width="1.5" stroke-linejoin="round"/>'+
    '<path d="M9 4 C 15 -2 14 -9 9 -10 C 5 -8 5 -1 9 4 Z" fill="#FFB79B" stroke="#C4683F" stroke-width="1.5" stroke-linejoin="round"/>'+
    '<path d="M0 5 C -7 -3 -5 -11 0 -12 C 5 -11 7 -3 0 5 Z" fill="#FFB79B" stroke="#C4683F" stroke-width="1.6" stroke-linejoin="round"/>'+
    '<path d="M0 4 V-9" fill="none" stroke="#C4683F" stroke-width="1" stroke-linecap="round"/>'+
    '<path d="M-22 4 Q0 -2 22 4 L22 10 Q0 4 -22 10 Z" fill="#FFD86B" stroke="#B87400" stroke-width="1.8" stroke-linejoin="round"/>'+
    '<circle cx="-12" cy="5.8" r="2.2" fill="#FFC93C" stroke="#B87400" stroke-width="1"/>'+
    '<circle cx="0" cy="4" r="2.2" fill="#FFC93C" stroke="#B87400" stroke-width="1"/>'+
    '<circle cx="12" cy="5.8" r="2.2" fill="#FFC93C" stroke="#B87400" stroke-width="1"/>'+
  '</g>';
}
```

---

### 4.2 围巾

#### ⑤ `nebula` 星云围巾 ｜ Lv23 ｜ 30⭐ ｜ myth

**配色**：主 `#7FC7EE` / 亮 `#CDEEFF` / 描边 `#2F86B4`；星点 `#FFF6DF` + `#B87400`

**绘制要点**（原点 = 绝对 (60,104)）
1. **主弧**：`M-30 0 q30 15 60 0`，`stroke-width=8`，`linecap=round`。弧中线 y 值：x=∓24→2.7，x=∓16→5.4，x=0→7.5
2. **内亮弧**：`M-26 6 q26 12 52 0`，`stroke-width=4`，`#CDEEFF` —— 叠在主弧下沿，形成"双层"
3. **2 颗小圆点**：`(∓24, 3)` r=1.8
4. **3 颗四角星**（同 `mooncrown` 的 sparkle 路径），尺寸 **中 > 侧**：
   - `translate(0,7.5) scale(1.05)`（中，最大）
   - `translate(∓16,5) scale(.7)`

**与现有 `wave` 的区别**：`wave` 是**等宽双色波浪线**；`nebula` 是**粗主弧 + 细亮弧 + 三颗大小不同的星**，且有立体挂饰。

**尺寸约束**：整体 60×19 单位。星点直径 5.6 / 8.4 单位 → 84 px 下 3.1 / 4.7 px。主弧 8 单位宽 → 84 px 下 4.5 px。

**色觉可访问性**：双层弧靠**粗细**（8 vs 4）；三颗星靠**大小**（中央最大）。

```js
nebula: function(){
  function sp(x,y,s){
    return '<path transform="translate('+x+','+y+') scale('+s+')" '+
      'd="M0 -4 Q.8 -1 4 0 Q.8 1 0 4 Q-.8 1 -4 0 Q-.8 -1 0 -4 Z" '+
      'fill="#FFF6DF" stroke="#B87400" stroke-width="1.1" stroke-linejoin="round"/>';
  }
  return '<g class="pk-scarf" transform="translate(60,104)">'+
    '<path d="M-30 0 q30 15 60 0" fill="none" stroke="#7FC7EE" stroke-width="8" stroke-linecap="round"/>'+
    '<path d="M-26 6 q26 12 52 0" fill="none" stroke="#CDEEFF" stroke-width="4" stroke-linecap="round"/>'+
    '<circle cx="-24" cy="3" r="1.8" fill="#FFF6DF" stroke="#B87400" stroke-width=".9"/>'+
    '<circle cx="24" cy="3" r="1.8" fill="#FFF6DF" stroke="#B87400" stroke-width=".9"/>'+
    sp(-16,5,.7)+sp(0,7.5,1.05)+sp(16,5,.7)+
  '</g>';
}
```

---

#### ⑥ `pearl` 珍珠围巾 ｜ Lv28 ｜ 40⭐ ｜ myth

**配色**：主 `#FFF6DF` / 亮 `#FFFFFF` / 描边 `#C9A86A`

**绘制要点**
1. **主弧**：`M-30 0 q30 15 60 0`，`stroke-width=9`，`#FFF6DF`
2. **高光弧**（叠在主弧中线上）：同路径 `stroke-width=3`，`#FFFFFF`，`opacity=.7`
3. **9 颗珍珠**：挂在弧下沿，`cy = 弧中线 y + 5.5`，**半径交替**（中间最大）：
   | x | cy | r |
   |---|---|---|
   | ∓24 | 8.2 | 3.0 |
   | ∓18 | 10.3 | 2.2 |
   | ∓12 | 11.8 | 3.0 |
   | ∓6 | 12.7 | 2.2 |
   | 0 | 13.0 | **3.4** |
4. **高光点**：仅 `r ≥ 3.0` 的 5 颗加 `circle r=0.9` 白点于 `(cx-1, cy-1.1)`

**尺寸约束**：整体 60×24 单位。珍珠直径 **4.4–6.8 单位**，间距 6 单位 → 84 px 下 2.5–3.8 px、间距 3.4 px，**刚好在可辨下限**（84 px 卡下建议只看轮廓，200 px 下 5.9–9.1 px 完全清晰）。最下沿 y=16.4（绝对 120.4），未越过阴影 124。

**色觉可访问性**：珍珠**大/小交替** + **中央最大**，形成非颜色的节奏编码；每颗都有描边，即使与围巾同色也不糊。

```js
pearl: function(){
  var px=[-24,-18,-12,-6,0,6,12,18,24],
      py=[8.2,10.3,11.8,12.7,13,12.7,11.8,10.3,8.2],
      pr=[3,2.2,3,2.2,3.4,2.2,3,2.2,3], o='', i;
  for(i=0;i<9;i++){
    o += '<circle cx="'+px[i]+'" cy="'+py[i]+'" r="'+pr[i]+'" fill="#FFF6DF" stroke="#C9A86A" stroke-width="1.1"/>';
    if(pr[i] >= 3){
      o += '<circle cx="'+(px[i]-1)+'" cy="'+(py[i]-1.1)+'" r=".9" fill="#FFFFFF" opacity=".9"/>';
    }
  }
  return '<g class="pk-scarf" transform="translate(60,104)">'+
    '<path d="M-30 0 q30 15 60 0" fill="none" stroke="#FFF6DF" stroke-width="9" stroke-linecap="round"/>'+
    '<path d="M-30 0 q30 15 60 0" fill="none" stroke="#FFFFFF" stroke-width="3" stroke-linecap="round" opacity=".7"/>'+
    o+'</g>';
}
```

---

#### ⑦ `ribbon` 缎带围巾 ｜ Lv33 ｜ 50⭐ ｜ myth

**配色**：主 `#FF6B9D` / 亮 `#FFB3C8` / 描边 `#D96A8C`

**绘制要点**（原点 = 绝对 (60,104)，蝴蝶结中心约在绝对 (60,105)，即肚皮下缘）
1. **主弧**：`M-30 0 q30 14 60 0`，`stroke-width=7`
2. **两条飘带**（先画，压在结下）：
   - 左 `M-6 6 C -10 12 -8 17 -4 19 C -1 17 -2 12 -6 6 Z`
   - 右 `M6 6 C 10 12 8 17 4 19 C 1 17 2 12 6 6 Z`
   - 下沿 y=19（绝对 123），**未越过阴影 124**
3. **蝴蝶结双环**：
   - 左环 `M-2 1 C -12 -8 -21 -3 -14 3 C -10 6 -5 5 -2 1 Z`
   - 右环 `M2 1 C 12 -8 21 -3 14 3 C 10 6 5 5 2 1 Z`
4. **中心方扣**：`rect x=-4.2 y=-1.6 w=8.4 h=8.4 rx=3.2`，`#FF6B9D`（比环深一档）

**尺寸约束**：蝴蝶结 42×27 单位 → 200 px 下 **56×36 px**，是全部装扮里视觉体量最大的单个元素；飘带宽 8 单位 → 84 px 下 4.5 px。

**色觉可访问性**：蝴蝶结靠**"两环 + 方扣 + 两条飘带"的复合剪影**，与 `heart` / `star` / `wave` 的单吊坠完全区分；不依赖颜色。

```js
ribbon: function(){
  return '<g class="pk-scarf" transform="translate(60,104)">'+
    '<path d="M-30 0 q30 14 60 0" fill="none" stroke="#FF6B9D" stroke-width="7" stroke-linecap="round"/>'+
    '<path d="M-6 6 C -10 12 -8 17 -4 19 C -1 17 -2 12 -6 6 Z" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M6 6 C 10 12 8 17 4 19 C 1 17 2 12 6 6 Z" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.4" stroke-linejoin="round"/>'+
    '<path d="M-2 1 C -12 -8 -21 -3 -14 3 C -10 6 -5 5 -2 1 Z" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.6" stroke-linejoin="round"/>'+
    '<path d="M2 1 C 12 -8 21 -3 14 3 C 10 6 5 5 2 1 Z" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.6" stroke-linejoin="round"/>'+
    '<rect x="-4.2" y="-1.6" width="8.4" height="8.4" rx="3.2" fill="#FF6B9D" stroke="#D96A8C" stroke-width="1.6"/>'+
  '</g>';
}
```

---

#### ⑧ `cloudscarf` 云绒围巾 ｜ Lv38 ｜ 60⭐ ｜ myth

**配色**：主 `#E8F1FB` / 亮 `#FFFFFF` / 描边 `#9DB8D4`

**绘制要点**（**唯一一条"非线状"围巾**，与 `cloudhat` / `cloudwing` 成组）
1. **云带主体** = 5 个叠加 `<ellipse>`（各自带描边，保留内部叠加线，与既有 `cloudhat` 一致）：
   | cx | cy | rx | ry |
   |---|---|---|---|
   | ∓22 | 4 | 10 | 7 |
   | ∓11 | 7 | 11 | 8 |
   | 0 | 9 | 12 | 9 |
2. **3 条云穗**（垂在下方）：`circle` at `(∓14,15) r=2.8`、`(0,17) r=3.2`
3. **高光**：`ellipse cx=-4 cy=5 rx=5.4 ry=3.2` `#FFFFFF` `opacity=.85`（最后画，压在云带上）

**尺寸约束**：整体 64×26 单位。中央云穗直径 6.4 单位 → 84 px 下 3.6 px。最下沿 y=20.2（绝对 124.2）——**略微压到阴影，视觉上是"云坐在地上"，可接受**；若要求严格，把云穗 cy 改 13/15/13。

**色觉可访问性**：云靠**多个圆叠加的扇贝状轮廓**（唯一），与所有线状围巾区分；不依赖颜色。

```js
cloudscarf: function(){
  var c=[[-22,4,10,7],[-11,7,11,8],[0,9,12,9],[11,7,11,8],[22,4,10,7]], o='', i;
  for(i=0;i<5;i++){
    o += '<ellipse cx="'+c[i][0]+'" cy="'+c[i][1]+'" rx="'+c[i][2]+'" ry="'+c[i][3]+
         '" fill="#E8F1FB" stroke="#9DB8D4" stroke-width="1.6"/>';
  }
  return '<g class="pk-scarf" transform="translate(60,104)">'+o+
    '<circle cx="-14" cy="15" r="2.8" fill="#E8F1FB" stroke="#9DB8D4" stroke-width="1.4"/>'+
    '<circle cx="0" cy="17" r="3.2" fill="#E8F1FB" stroke="#9DB8D4" stroke-width="1.4"/>'+
    '<circle cx="14" cy="15" r="2.8" fill="#E8F1FB" stroke="#9DB8D4" stroke-width="1.4"/>'+
    '<ellipse cx="-4" cy="5" rx="5.4" ry="3.2" fill="#FFFFFF" opacity=".85"/>'+
  '</g>';
}
```

---

### 4.3 翅膀

> 通用结构：`<g class="pk-wing">` 内含左右两组，各套 `rotate(∓角度, 支点)`。
> **镜像公式**：右侧 `cx' = 120 - cx`，`rotate(+角度)`，`scale` 不变。
> 翅膀在身体**之前**绘制 → 内侧会被身体遮住，造型必须往外探（x < 28 / x > 92）。

#### ⑨ `aurorawing` 极光翅膀 ｜ Lv24 ｜ 30⭐ ｜ myth

**配色**：带1 `#7FC7EE`（w=7）/ 带2 `#8ED46E`（w=5）/ 带3 `#CDEEFF`（w=3）；端点星 `#FFF6DF` + `#B87400`

**绘制要点**（左侧，整组 `rotate(-20 26 78)`）
1. **带1（最长最粗）**：`M26 54 C 8 62 4 86 14 108` —— 从肩 (26,54) 向外下扫到 (14,108)
2. **带2（中）**：`M26 60 C 14 66 10 86 18 102`
3. **带3（最短最细）**：`M26 68 C 20 72 17 86 22 96`
4. **端点星**：`circle cx=13 cy=107 r=2.6`
5. 右侧绕 `rotate(20 94 78)`，x 取 `120-x`：`M94 54 C 112 62 116 86 106 108` 等

**尺寸约束**：单侧 22×54 单位。**三条带靠"长度 + 粗细"双重区分**（终点 y=108/102/96，宽度 7/5/3）→ 200 px 下终点差 8 px / 16 px，粗细差 2.7 px / 5.3 px。最细带 3 单位 → 84 px 下 1.7 px（偏细，但它是第三层，糊掉不影响识别）。

**色觉可访问性**：三条带**长度递减 + 粗细递减**，纯灰度下依然能分辨层次；不依赖三色的色相差异。

```js
aurorawing: function(lineC, pref){
  return '<g class="pk-wing">'+
    '<g transform="rotate(-20 26 78)">'+
      '<path d="M26 54 C 8 62 4 86 14 108" fill="none" stroke="#7FC7EE" stroke-width="7" stroke-linecap="round"/>'+
      '<path d="M26 60 C 14 66 10 86 18 102" fill="none" stroke="#8ED46E" stroke-width="5" stroke-linecap="round" opacity=".92"/>'+
      '<path d="M26 68 C 20 72 17 86 22 96" fill="none" stroke="#CDEEFF" stroke-width="3" stroke-linecap="round"/>'+
      '<circle cx="13" cy="107" r="2.6" fill="#FFF6DF" stroke="#B87400" stroke-width="1"/>'+
    '</g>'+
    '<g transform="rotate(20 94 78)">'+
      '<path d="M94 54 C 112 62 116 86 106 108" fill="none" stroke="#7FC7EE" stroke-width="7" stroke-linecap="round"/>'+
      '<path d="M94 60 C 106 66 110 86 102 102" fill="none" stroke="#8ED46E" stroke-width="5" stroke-linecap="round" opacity=".92"/>'+
      '<path d="M94 68 C 100 72 103 86 98 96" fill="none" stroke="#CDEEFF" stroke-width="3" stroke-linecap="round"/>'+
      '<circle cx="107" cy="107" r="2.6" fill="#FFF6DF" stroke="#B87400" stroke-width="1"/>'+
    '</g></g>';
}
```

---

#### ⑩ `angelwing` 天使之翼 ｜ Lv29 ｜ 40⭐ ｜ myth

**配色**：主 `#FFF6DF` / 亮 `#FFFFFF` / 描边 `#C9A86A`

**绘制要点**（左侧，整组 `rotate(-12 26 80)`）
1. **4 片圆头羽**（`<ellipse>` + 各自 `rotate` 呈扇形展开），**尺寸递减**：
   | cx | cy | rx | ry | 自身 rotate |
   |---|---|---|---|---|
   | 9 | 66 | 6.5 | 17 | −30 |
   | 14 | 74 | 6.5 | 20 | −16 |
   | 19 | 82 | 6.2 | 18 | −2 |
   | 24 | 89 | 5.6 | 14 | +12 |
2. **肩部小云**（最后画，压在羽根上）：`ellipse cx=25 cy=70 rx=8 ry=6`，`#FFFFFF`
3. 右侧：`cx' = 120 - cx`，`rotate` 取反，整组 `rotate(12 94 80)`

**与现有 `big` 的区别**：`big` 是**一整片大椭圆 + 两条金色羽线**；`angelwing` 是**4 片独立可辨的圆头羽**，剪影是扇贝状。

**尺寸约束**：单侧 26×46 单位。羽毛宽 11.2–13 单位 → 84 px 下 6.3–7.3 px，清晰；羽毛间隙靠 rotate 差（14°/14°/14°）拉开。**每侧羽片数上限 4**（超过 4 片在 84 px 下会糊成一片）。

**色觉可访问性**：4 片羽靠**长度梯度**（17/20/18/14）+ **独立描边**，灰度下仍可数出片数。

```js
angelwing: function(lineC, pref){
  var L=[[9,66,6.5,17,-30],[14,74,6.5,20,-16],[19,82,6.2,18,-2],[24,89,5.6,14,12]],
      o='', r='', i, f;
  for(i=0;i<4;i++){
    f = L[i];
    o += '<ellipse cx="'+f[0]+'" cy="'+f[1]+'" rx="'+f[2]+'" ry="'+f[3]+
         '" transform="rotate('+f[4]+' '+f[0]+' '+f[1]+')" fill="#FFF6DF" stroke="#C9A86A" stroke-width="1.6"/>';
    r += '<ellipse cx="'+(120-f[0])+'" cy="'+f[1]+'" rx="'+f[2]+'" ry="'+f[3]+
         '" transform="rotate('+(-f[4])+' '+(120-f[0])+' '+f[1]+')" fill="#FFF6DF" stroke="#C9A86A" stroke-width="1.6"/>';
  }
  return '<g class="pk-wing">'+
    '<g transform="rotate(-12 26 80)">'+o+
      '<ellipse cx="25" cy="70" rx="8" ry="6" fill="#FFFFFF" stroke="#C9A86A" stroke-width="1.4"/></g>'+
    '<g transform="rotate(12 94 80)">'+r+
      '<ellipse cx="95" cy="70" rx="8" ry="6" fill="#FFFFFF" stroke="#C9A86A" stroke-width="1.4"/></g></g>';
}
```

---

#### ⑪ `crystalwing` 水晶翅膀 ｜ Lv34 ｜ 50⭐ ｜ myth

**配色**：主 `#CDEEFF` / 亮 `#FFFFFF` / 描边 `#2F86B4`

**绘制要点**（左侧，整组 `rotate(-18 26 80)`）
1. **晶体基本形**（六棱柱剪影）：`M0 -17 L6 -5 L4 13 L-4 13 L-6 -5 Z`（高 30，最宽 12）
2. **棱面高光**：中脊 `M0 -17 V13`（白 w=1.2 op .85）+ 顶棱 `M-6 -5 L0 -17 L6 -5`（白 w=1 op .7）
3. **3 枚晶体，扇形排布，大小递变**：
   | translate | rotate | scale |
   |---|---|---|
   | (13,70) | −30 | .85 |
   | (19,79) | −10 | **1.10** |
   | (26,88) | +12 | .80 |
4. 右侧：`translate(120-x, y)`，`rotate` 取反，整组 `rotate(18 94 80)`

> ⚠ `scale` 会等比缩放 `stroke-width`：实际描边 1.53 / 1.98 / 1.44 单位，最小 1.44 → 120 px 下 1.15 px，仍可辨。若要严格统一，可在 path 上加 `vector-effect="non-scaling-stroke"`（内联 SVG 支持，无兼容问题）。

**与现有 `starwing` 的区别**：`starwing` 是**椭圆 + 三个金色圆点**；`crystalwing` 是**多边形晶体 + 棱面线**，剪影是折线而非圆弧。

**尺寸约束**：单侧 26×50 单位。晶体宽 9.6–13.2 单位 → 84 px 下 5.4–7.4 px。中脊线 1.2 单位在 84 px 下 0.67 px（会淡出，属可接受的细节损失）。

**色觉可访问性**：3 枚晶体靠**大小 + 朝向**；晶体本身是**多边形**（与所有椭圆翅膀的剪影不同）。

```js
crystalwing: function(lineC, pref){
  var C=[[13,70,.85,-30],[19,79,1.1,-10],[26,88,.8,12]], o='', r='', i, c, face;
  face = '<path d="M0 -17 L6 -5 L4 13 L-4 13 L-6 -5 Z" fill="#CDEEFF" stroke="#2F86B4" stroke-width="1.8" stroke-linejoin="round"/>'+
         '<path d="M0 -17 V13" fill="none" stroke="#FFFFFF" stroke-width="1.2" opacity=".85"/>'+
         '<path d="M-6 -5 L0 -17 L6 -5" fill="none" stroke="#FFFFFF" stroke-width="1" opacity=".7"/>';
  for(i=0;i<3;i++){
    c = C[i];
    o += '<g transform="translate('+c[0]+','+c[1]+') rotate('+c[3]+') scale('+c[2]+')">'+face+'</g>';
    r += '<g transform="translate('+(120-c[0])+','+c[1]+') rotate('+(-c[3])+') scale('+c[2]+')">'+face+'</g>';
  }
  return '<g class="pk-wing">'+
    '<g transform="rotate(-18 26 80)">'+o+'</g>'+
    '<g transform="rotate(18 94 80)">'+r+'</g></g>';
}
```

---

#### ⑫ `dragonwing` 祥龙翅膀 ｜ Lv39 ｜ 60⭐ ｜ myth

**配色**：主 `#FFB79B` / 亮 `#FFDCC9` / 描边 `#C4683F`；骨线 `#FFC93C`；鳞片 `#FFDCC9`

**绘制要点**（左侧，整组 `rotate(-14 27 76)`）
1. **三瓣翼膜**（三段三次贝塞尔，圆角连接）：
   ```
   M27 74 C 18 58 11 51 5 55   ← 上缘，收到上瓣尖 (5,55)
     C 9 62 8 68 4 72          ← 上瓣 → 中瓣尖 (4,72)
     C 9 74 12 74 14 78        ← 中瓣 → 内凹
     C 9 82 7 88 11 92         ← 内凹 → 下瓣尖 (11,92)
     C 16 90 23 84 27 74 Z     ← 回到翼根
   ```
   三个尖：(5,55) (4,72) (11,92)；翼根 (27,74)。`stroke-width=2.2` + `stroke-linejoin="round"` → 尖角圆润化，**不出现锐利感**
2. **3 条金色骨线**（从翼根射向三个尖）：`M27 74 L6 56` / `M27 74 L5 72` / `M27 74 L11 91`，`stroke-width=2` `opacity=.9`
3. **3 片圆鳞**：`circle` at `(17,64) r=2.4` `(24,74) r=2.0` `(19,84) r=2.2`
4. 右侧：x 取 `120-x`，整组 `rotate(14 93 76)`

**尺寸约束**：单侧 23×41 单位。三个瓣尖间距约 17 单位 → 84 px 下 9.5 px，剪影清晰。骨线 2 单位 → 84 px 下 1.1 px（偏细，主要功能在 120 px 以上体现）。

**色觉可访问性**：三瓣**扇贝状剪影**（唯一）+ 三条骨线（形状线索）+ 圆鳞大小递变；不依赖颜色。

**"神兽但不吓人"的处理**：翼膜用蜜桃粉而非暗红/黑；所有尖角 round join；加金色骨线和圆鳞做"装饰感"，弱化蝙蝠翼的攻击性。

```js
dragonwing: function(lineC, pref){
  return '<g class="pk-wing">'+
    '<g transform="rotate(-14 27 76)">'+
      '<path d="M27 74 C 18 58 11 51 5 55 C 9 62 8 68 4 72 C 9 74 12 74 14 78 C 9 82 7 88 11 92 C 16 90 23 84 27 74 Z" '+
        'fill="#FFB79B" stroke="#C4683F" stroke-width="2.2" stroke-linejoin="round"/>'+
      '<path d="M27 74 L6 56 M27 74 L5 72 M27 74 L11 91" fill="none" stroke="#FFC93C" stroke-width="2" stroke-linecap="round" opacity=".9"/>'+
      '<circle cx="17" cy="64" r="2.4" fill="#FFDCC9" stroke="#C4683F" stroke-width="1"/>'+
      '<circle cx="24" cy="74" r="2" fill="#FFDCC9" stroke="#C4683F" stroke-width="1"/>'+
      '<circle cx="19" cy="84" r="2.2" fill="#FFDCC9" stroke="#C4683F" stroke-width="1"/>'+
    '</g>'+
    '<g transform="rotate(14 93 76)">'+
      '<path d="M93 74 C 102 58 109 51 115 55 C 111 62 112 68 116 72 C 111 74 108 74 106 78 C 111 82 113 88 109 92 C 104 90 97 84 93 74 Z" '+
        'fill="#FFB79B" stroke="#C4683F" stroke-width="2.2" stroke-linejoin="round"/>'+
      '<path d="M93 74 L114 56 M93 74 L115 72 M93 74 L109 91" fill="none" stroke="#FFC93C" stroke-width="2" stroke-linecap="round" opacity=".9"/>'+
      '<circle cx="103" cy="64" r="2.4" fill="#FFDCC9" stroke="#C4683F" stroke-width="1"/>'+
      '<circle cx="96" cy="74" r="2" fill="#FFDCC9" stroke="#C4683F" stroke-width="1"/>'+
      '<circle cx="101" cy="84" r="2.2" fill="#FFDCC9" stroke="#C4683F" stroke-width="1"/>'+
    '</g></g>';
}
```

---

### 4.4 背景

> 通用：直接追加为 `petBgSVG` 的 `else if` 分支。
> 装饰区 = 四角 / 上边 y<50 / 下边 y>150 / 左右 x<56 或 x>144；主体框 `x∈[60,140] / y∈[55,145]` 内只允许渐变底、柔光板、opacity≤.42 的极光带。

#### ⑬ `skygarden` 云上花园 ｜ Lv25 ｜ 30⭐ ｜ myth ｜ **浅底**

**配色**：天 `#E4F2FB` ｜ 草地 `#EFF7E6` + `#BFE0A8` ｜ 云 `#FFFFFF` ｜ 花 `#FFB3C8` + 心 `#FFC93C` + 描边 `#D96A8C` / `#B87400`
**柔光板**：`#5A3C28` `opacity=".10"`（浅底 → 用深色托底）

**绘制要点**
1. 天：`rect 200×200 #E4F2FB`
2. 草地：`rect y=146 h=54 #EFF7E6` + 草坡 `M0 150 q40 -12 80 0 q40 12 80 0 q20 -6 40 0 v50 H0 z #BFE0A8`
3. **柔光板**：`ellipse cx=100 cy=104 rx=62 ry=66 fill=url(#skyG)`（`#skyG` = 白 → 透明的径向渐变，用于浅底时改填 `#5A3C28` op .10）
4. **6 朵云**（上边 + 左右上角）：`(34,34) r17×12` `(49,29) r13×10` `(21,38) r11×8` `(167,45) r15×11` `(152,40) r11×8` `(100,19) r21×9`，`#FFFFFF` op .92
5. **5 朵小花**（底部 + 左右下角，`(24,168) (52,180) (148,176) (176,166) (100,188)`）：每朵 = 5 个 `circle r=3.4` 粉圆围绕中心 (0,0) 排布（`(0,-4.4) (4.2,-1.4) (2.6,3.4) (-2.6,3.4) (-4.2,-1.4)`）+ 中心 `circle r=2.4` 金

**尺寸约束**：单朵花直径 11.2 单位（背景 200 单位 → 显示 200 px 时 11.2 px）。**花只放在 y>160 或 x<56/x>144**，绝不进入主体框。云朵最小 ry=8 → 16 单位高，不会被误认为宠物。

**色觉可访问性**：花是**五瓣圆盘**（唯一形状）；云是**圆叠加团块**。柔光板提供与浅色宠物的明度分离。

```js
} else if(id === 'skygarden'){
  var g, fx=[24,52,148,176,100], fy=[168,180,176,166,188];
  o += '<defs><radialGradient id="skyG" cx="50%" cy="50%" r="50%">'+
       '<stop offset="0" stop-color="#5A3C28" stop-opacity=".10"/>'+
       '<stop offset="1" stop-color="#5A3C28" stop-opacity="0"/></radialGradient></defs>';
  o += '<rect width="200" height="200" fill="#E4F2FB"/>';
  o += '<rect y="146" width="200" height="54" fill="#EFF7E6"/>';
  o += '<path d="M0 150 q40 -12 80 0 q40 12 80 0 q20 -6 40 0 v50 H0 z" fill="#BFE0A8"/>';
  o += '<ellipse cx="100" cy="104" rx="62" ry="66" fill="url(#skyG)"/>';
  o += '<g fill="#FFFFFF" opacity=".92">'+
       '<ellipse cx="34" cy="34" rx="17" ry="12"/><ellipse cx="49" cy="29" rx="13" ry="10"/>'+
       '<ellipse cx="21" cy="38" rx="11" ry="8"/><ellipse cx="167" cy="45" rx="15" ry="11"/>'+
       '<ellipse cx="152" cy="40" rx="11" ry="8"/><ellipse cx="100" cy="19" rx="21" ry="9"/></g>';
  for(g=0;g<5;g++){
    o += '<g transform="translate('+fx[g]+','+fy[g]+')">'+
      '<circle cx="0" cy="-4.4" r="3.4" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.1"/>'+
      '<circle cx="4.2" cy="-1.4" r="3.4" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.1"/>'+
      '<circle cx="2.6" cy="3.4" r="3.4" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.1"/>'+
      '<circle cx="-2.6" cy="3.4" r="3.4" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.1"/>'+
      '<circle cx="-4.2" cy="-1.4" r="3.4" fill="#FFB3C8" stroke="#D96A8C" stroke-width="1.1"/>'+
      '<circle r="2.4" fill="#FFC93C" stroke="#B87400" stroke-width="1"/></g>';
  }
```

---

#### ⑭ `starsea` 星海背景 ｜ Lv30 ｜ 40⭐ ｜ myth ｜ **深底**

**配色**：天 `#1E3A5E → #4E7BA8`（竖渐变）｜ 星/流星 `#FFF6DF` ｜ 月 `#FFE9A8` ｜ 海浪 `#2C5580` / `#1E3A5E`
**柔光板**：`#FFFFFF` `opacity=".22"`（深底 → 用浅色托底）

**绘制要点**
1. 渐变底：`linearGradient #seaG` 上 `#1E3A5E` → 下 `#4E7BA8`
2. **10 颗四角星**（sparkle 路径，scale 0.6–1.05 → 直径 4.8–8.4 单位），坐标**全部避开主体框**：
   `(18,22) (44,40) (72,16) (120,18) (158,30) (182,58) (186,120) (170,168) (32,96) (16,140)`
3. **月牙**（右上角）：`M166 20 a16 16 0 1 0 12 26 a19 19 0 1 1 -12 -26 Z`，`#FFE9A8` op .9
4. **流星**（左上）：尾迹 `M22 68 l32 -19`（w=2.6 op .5）+ 头 `circle (22,68) r=2.8`
5. **柔光板**：`ellipse cx=100 cy=104 rx=60 ry=64 fill=url(#seaGlow)`
6. **双层海浪**（下边 y>155）：`M0 158 q34 -12 68 0 q34 12 68 0 q32 -10 64 0 v42 H0 z #2C5580 op .9` + `M0 174 q40 -10 80 0 q40 10 80 0 q20 -6 40 0 v26 H0 z #1E3A5E`
7. **3 个海面星点倒影**：`(40,168) r2` `(100,182) r1.8` `(158,170) r2.2`，`#FFF6DF` op .45–.5

**与现有 `space` 的区别**：`space` 是**紫渐变（`#2E2A5E→#5B4B9E`）+ 圆点星 + 月牙**；`starsea` 是**蓝渐变 + 四角星 + 流星 + 双层海浪**，色相与元素都拉开，且海浪提供"下半部有内容"的构图差异。

**尺寸约束**：星直径 4.8–8.4 单位（背景 200 单位 → 200 px 显示时 4.8–8.4 px）。月牙 28×26 单位。

**色觉可访问性**：星是**四角星形**（不是圆点）；月牙是**C 形剪影**；海浪是**两条不同粗细的波浪带**。柔光板保证浅紫宠物在深底上不糊。

```js
} else if(id === 'starsea'){
  var s2, sx2=[18,44,72,120,158,182,186,170,32,16],
          sy2=[22,40,16,18,30,58,120,168,96,140],
          ss2=[3.4,2.6,4.2,3,3.6,2.6,2.4,3,2.4,2.8];
  o += '<defs>'+
    '<linearGradient id="seaG" x1="0" y1="0" x2="0" y2="1">'+
      '<stop offset="0" stop-color="#1E3A5E"/><stop offset="1" stop-color="#4E7BA8"/></linearGradient>'+
    '<radialGradient id="seaGlow" cx="50%" cy="50%" r="50%">'+
      '<stop offset="0" stop-color="#FFFFFF" stop-opacity=".22"/>'+
      '<stop offset="1" stop-color="#FFFFFF" stop-opacity="0"/></radialGradient></defs>';
  o += '<rect width="200" height="200" fill="url(#seaG)"/>';
  for(s2=0;s2<10;s2++){
    o += '<path transform="translate('+sx2[s2]+','+sy2[s2]+') scale('+(ss2[s2]/4)+')" '+
      'd="M0 -4 Q.8 -1 4 0 Q.8 1 0 4 Q-.8 1 -4 0 Q-.8 -1 0 -4 Z" fill="#FFF6DF" opacity=".9"/>';
  }
  o += '<path d="M166 20 a16 16 0 1 0 12 26 a19 19 0 1 1 -12 -26 Z" fill="#FFE9A8" opacity=".9"/>';
  o += '<path d="M22 68 l32 -19" fill="none" stroke="#FFF6DF" stroke-width="2.6" stroke-linecap="round" opacity=".5"/>';
  o += '<circle cx="22" cy="68" r="2.8" fill="#FFF6DF"/>';
  o += '<ellipse cx="100" cy="104" rx="60" ry="64" fill="url(#seaGlow)"/>';
  o += '<path d="M0 158 q34 -12 68 0 q34 12 68 0 q32 -10 64 0 v42 H0 z" fill="#2C5580" opacity=".9"/>';
  o += '<path d="M0 174 q40 -10 80 0 q40 10 80 0 q20 -6 40 0 v26 H0 z" fill="#1E3A5E"/>';
  o += '<circle cx="40" cy="168" r="2" fill="#FFF6DF" opacity=".5"/>'+
       '<circle cx="100" cy="182" r="1.8" fill="#FFF6DF" opacity=".45"/>'+
       '<circle cx="158" cy="170" r="2.2" fill="#FFF6DF" opacity=".5"/>';
```

---

#### ⑮ `gemcave` 宝石洞窟 ｜ Lv35 ｜ 50⭐ ｜ myth ｜ **中深底**

**配色**：洞 `#1F5150 → #4E9E96`（竖渐变）｜ 洞壁/地面 `#173F3E` ｜ 钟乳石 `#2A6B68` ｜ 水晶 `#7FC7EE` + `#CDEEFF` 描边 + 白中脊
**柔光板**：`#FFFFFF` `opacity=".20"`

**绘制要点**
1. 渐变底 + 左右洞壁弧：`M0 0 q28 92 0 200 z` 与 `M200 0 q-28 92 0 200 z`，`#173F3E` op .5（只鼓到 x=14 / x=186，**不侵入主体框**）
2. **4 根钟乳石**（仅顶边 y<40）：`M12 0 l14 0 l-7 34 z`／`M52 0 l12 0 l-6 22 z`／`M150 0 l12 0 l-6 26 z`／`M176 0 l16 0 l-8 38 z`，`#2A6B68`
3. **柔光板**：`ellipse cx=100 cy=104 rx=58 ry=62 fill=url(#caveGlow)`
4. **地面**：`M0 172 q50 -10 100 0 q50 10 100 0 v28 H0 z #173F3E`
5. **5 簇水晶体**（底部 + 左右下角）：基本形 `M0 -15 L7 0 L0 13 L-7 0 Z` + 中脊 `M0 -15 L0 13`（白 w=1.2 op .8）
   | translate | rotate | scale |
   |---|---|---|
   | (26,152) | −12 | 1.2 |
   | (44,166) | +8 | .8 |
   | (168,150) | +14 | 1.1 |
   | (150,168) | −6 | .75 |
   | (100,184) | 0 | .6 |
6. **4 个微光点**：`(26,70) (176,84) (34,118) (166,124)`，`#CDEEFF` op .5–.55

**尺寸约束**：水晶体高 28×14 单位（最大簇 scale 1.2 → 33.6×16.8）。全部位于 y>140 或 x<56/x>144。

**色觉可访问性**：水晶是**菱形**（与钟乳石的三角、洞壁的弧完全不同）；5 簇大小递变。青绿背景与紫色宠物是**补色关系**，色相冲突最大 → 靠柔光板 + 宠物金色描边双重分离。

```js
} else if(id === 'gemcave'){
  var c2, cx2=[26,44,168,150,100], cy2=[152,166,150,168,184],
          cs2=[1.2,.8,1.1,.75,.6], cr2=[-12,8,14,-6,0];
  o += '<defs>'+
    '<linearGradient id="caveG" x1="0" y1="0" x2="0" y2="1">'+
      '<stop offset="0" stop-color="#1F5150"/><stop offset="1" stop-color="#4E9E96"/></linearGradient>'+
    '<radialGradient id="caveGlow" cx="50%" cy="50%" r="50%">'+
      '<stop offset="0" stop-color="#FFFFFF" stop-opacity=".20"/>'+
      '<stop offset="1" stop-color="#FFFFFF" stop-opacity="0"/></radialGradient></defs>';
  o += '<rect width="200" height="200" fill="url(#caveG)"/>';
  o += '<path d="M0 0 q28 92 0 200 z" fill="#173F3E" opacity=".5"/>';
  o += '<path d="M200 0 q-28 92 0 200 z" fill="#173F3E" opacity=".5"/>';
  o += '<g fill="#2A6B68"><path d="M12 0 l14 0 l-7 34 z"/><path d="M52 0 l12 0 l-6 22 z"/>'+
       '<path d="M150 0 l12 0 l-6 26 z"/><path d="M176 0 l16 0 l-8 38 z"/></g>';
  o += '<ellipse cx="100" cy="104" rx="58" ry="62" fill="url(#caveGlow)"/>';
  o += '<path d="M0 172 q50 -10 100 0 q50 10 100 0 v28 H0 z" fill="#173F3E"/>';
  for(c2=0;c2<5;c2++){
    o += '<g transform="translate('+cx2[c2]+','+cy2[c2]+') rotate('+cr2[c2]+') scale('+cs2[c2]+')">'+
      '<path d="M0 -15 L7 0 L0 13 L-7 0 Z" fill="#7FC7EE" stroke="#CDEEFF" stroke-width="1.8" stroke-linejoin="round"/>'+
      '<path d="M0 -15 L0 13" fill="none" stroke="#FFFFFF" stroke-width="1.2" opacity=".8"/></g>';
  }
  o += '<circle cx="26" cy="70" r="1.8" fill="#CDEEFF" opacity=".55"/>'+
       '<circle cx="176" cy="84" r="1.6" fill="#CDEEFF" opacity=".5"/>'+
       '<circle cx="34" cy="118" r="1.6" fill="#CDEEFF" opacity=".5"/>'+
       '<circle cx="166" cy="124" r="1.8" fill="#CDEEFF" opacity=".55"/>';
```

---

#### ⑯ `aurorabg` 极光天幕 ｜ Lv40 ｜ 60⭐ ｜ myth ｜ **上深下浅**

**配色**：天 `#24436E → #6E93B4`（竖渐变）｜ 极光带 `#8ED46E` / `#7FC7EE` / `#FFB3C8`（op .36–.42）｜ 雪地 `#EAF3FB` + `#C6E7F7` ｜ 雪松 `#377020` + 干 `#8A5A2B`
**柔光板**：`#FFFFFF` `opacity=".20"`

**绘制要点**
1. 渐变底
2. **6 颗四角星**（仅上边 y<46）：`(30,20) (66,34) (112,16) (150,32) (182,22) (96,44)`，scale .8（直径 6.4 单位）
3. **3 条极光带**（横贯全宽，**opacity ≤ .42**，宽度递变 16/11/7）：
   - `M-10 58 q55 -34 110 -14 q50 16 110 -8` `#8ED46E` w=16 op .36
   - `M-10 74 q55 -34 110 -12 q50 16 110 -6` `#7FC7EE` w=11 op .42
   - `M-10 88 q55 -30 110 -10 q50 14 110 -4` `#FFB3C8` w=7 op .38
   > 这三条**是唯一允许穿过主体框的装饰**，因为它们是低透明度的横向大色带，不会形成"抢主体注意力的图形"，反而提供高阶光效氛围。
4. **柔光板**：`ellipse cx=100 cy=104 rx=60 ry=64 fill=url(#auroGlow)`
5. **双层雪坡**（下边 y>150）：`M0 152 q46 -14 92 -2 q46 12 108 -4 v54 H0 z #EAF3FB` + `M0 174 q50 -10 100 0 q50 10 100 0 v26 H0 z #C6E7F7`
6. **3 棵雪松**（左下 ×2、右下 ×1）：`translate(26,150) s.9` / `(52,158) s.7` / `(170,152) s.85`
   树 = 树干 `rect x=-2 y=0 w=4 h=12 rx=2 #8A5A2B` + 三层塔形 `M0 -26 l11 15 l-6 0 l9 12 l-28 0 l9 -12 l-6 0 z #377020`

**尺寸约束**：极光带最宽 16 单位（200 px 下 16 px）。雪松高 38 单位（最大 scale .9 → 34 单位 → 200 px 下 34 px）。雪松全部在 x<56 或 x>144。

**色觉可访问性**：极光带靠**粗细三档**（16/11/7）+ **垂直位置**区分；雪松是**塔形剪影**；星是**四角星形**。柔光板保证上深下浅的渐变中段不会吞掉宠物。

```js
} else if(id === 'aurorabg'){
  var t2, tx2=[26,52,170], ty2=[150,158,152], ts2=[.9,.7,.85],
          ta=[30,66,112,150,182,96], tb=[20,34,16,32,22,44];
  o += '<defs>'+
    '<linearGradient id="auroG" x1="0" y1="0" x2="0" y2="1">'+
      '<stop offset="0" stop-color="#24436E"/><stop offset="1" stop-color="#6E93B4"/></linearGradient>'+
    '<radialGradient id="auroGlow" cx="50%" cy="50%" r="50%">'+
      '<stop offset="0" stop-color="#FFFFFF" stop-opacity=".20"/>'+
      '<stop offset="1" stop-color="#FFFFFF" stop-opacity="0"/></radialGradient></defs>';
  o += '<rect width="200" height="200" fill="url(#auroG)"/>';
  for(t2=0;t2<6;t2++){
    o += '<path transform="translate('+ta[t2]+','+tb[t2]+') scale(.8)" '+
      'd="M0 -4 Q.8 -1 4 0 Q.8 1 0 4 Q-.8 1 -4 0 Q-.8 -1 0 -4 Z" fill="#FFF6DF" opacity=".85"/>';
  }
  o += '<path d="M-10 58 q55 -34 110 -14 q50 16 110 -8" fill="none" stroke="#8ED46E" stroke-width="16" stroke-linecap="round" opacity=".36"/>';
  o += '<path d="M-10 74 q55 -34 110 -12 q50 16 110 -6" fill="none" stroke="#7FC7EE" stroke-width="11" stroke-linecap="round" opacity=".42"/>';
  o += '<path d="M-10 88 q55 -30 110 -10 q50 14 110 -4" fill="none" stroke="#FFB3C8" stroke-width="7" stroke-linecap="round" opacity=".38"/>';
  o += '<ellipse cx="100" cy="104" rx="60" ry="64" fill="url(#auroGlow)"/>';
  o += '<path d="M0 152 q46 -14 92 -2 q46 12 108 -4 v54 H0 z" fill="#EAF3FB"/>';
  o += '<path d="M0 174 q50 -10 100 0 q50 10 100 0 v26 H0 z" fill="#C6E7F7"/>';
  for(t2=0;t2<3;t2++){
    o += '<g transform="translate('+tx2[t2]+','+ty2[t2]+') scale('+ts2[t2]+')">'+
      '<rect x="-2" y="0" width="4" height="12" rx="2" fill="#8A5A2B"/>'+
      '<path d="M0 -26 l11 15 l-6 0 l9 12 l-28 0 l9 -12 l-6 0 z" fill="#377020"/></g>';
  }
```

---

## 5. 可访问性矩阵

### 5.1 分级
本项目采用 **Standard（标准级）**，目标项下含 Comprehensive 的两条进阶项（★）。

### 5.2 特性矩阵

| 维度 | 要求 | 本规格落地方式 |
|---|---|---|
| **色觉障碍（红绿色盲 / 全色盲）** | 同组元素不得只靠颜色区分 | 全部 16 件均提供**形状 / 大小 / 数量 / 位置**的冗余编码（见下表）。已逐件在 §4 标注 |
| **明度对比** | 主体与背景 ΔL 足够 | ① 宠物 Lv5+ 恒有 2.6–3 金色描边；② 每张背景强制柔光板（§3.6）；③ 背景主体框规则（§3.5） |
| **动效** | 可被 `prefers-reduced-motion` 关闭 | 新装扮 **0 条新循环动画**，仅继承 `.pk-hat/.pk-scarf/.pk-wing` 的 200 ms `pk-wear`，该动画已在现有 `@media (prefers-reduced-motion: reduce)` 中关闭 |
| ★ **文本可读性** | 装扮名在小卡上可读 | 中文名**全部 ≤ 4 字**（最长"缎带围巾""极光翅膀"等 4 字），适配 `.pc-name{font-size:var(--fs-mini)}` 的 92 px 卡宽 |
| ★ **点击热区** | ≥ 44×44 px | 新装扮不改变卡片结构（`.pc-card` 92 px 宽），热区不变 |
| **认知负荷（5 岁）** | 单件可辨识元素 ≤ 5 组 | 帽子 ≤ 5 冠尖/宝石、围巾 ≤ 9 珍珠（1 组重复元素）、翅膀 ≤ 4 羽、背景 ≤ 12 装饰元素 |

### 5.3 逐件"非颜色区分"编码表

| id | 冗余编码方式 |
|---|---|
| `aurora` | 三飘带**高度三级**（中>侧）；宝石 **圆/菱(大)/圆** |
| `gemcrown` | 五冠尖**露出高度三级**（10.0/8.1/5.6）；宝石 **圆/菱/圆(大)/菱/圆** |
| `mooncrown` | **C 形剪影**（唯一）；三星 **大小差**（中最大） |
| `phoenix` | 三片羽 **尺寸三级**（14×17 / 12×14 / 10×10）+ 羽轴线 |
| `nebula` | 双层弧 **粗细差**（8 vs 4）；三星 **大小差** |
| `pearl` | 珍珠 **大/小交替** + 中央最大（3.4 vs 2.2） |
| `ribbon` | **"双环+方扣+双飘带"复合剪影**（唯一） |
| `cloudscarf` | **唯一非线状围巾**（5 圆叠加扇贝轮廓） |
| `aurorawing` | 三带 **长度 + 粗细双递减**（108/102/96；7/5/3） |
| `angelwing` | 4 片独立羽 **长度梯度**（17/20/18/14）+ 独立描边 |
| `crystalwing` | **多边形晶体**（唯一折线剪影）；3 枚 **大小 + 朝向** |
| `dragonwing` | **三瓣扇贝剪影** + 3 条骨线 + 圆鳞大小递变 |
| `skygarden` | 花 **五瓣圆盘**；云 **圆叠加团块** |
| `starsea` | 星 **四角星形**（非圆点）；月 **C 形**；海浪 **双层粗细差** |
| `gemcave` | 水晶 **菱形**（vs 钟乳石三角、洞壁弧）；5 簇大小递变 |
| `aurorabg` | 极光带 **粗细三档**（16/11/7）+ 垂直位置；雪松 **塔形剪影** |

---

## 6. 交付给工程侧的强制项与风险

### 6.1 必须同步改的 4 处（否则新装扮"看得见买不到"或"戴上错位"）

| # | 位置（现有行号） | 必须做的事 |
|---|---|---|
| A1 | `PET_COSTUME_LIST`（:3675） | 追加 16 条 `{id, name, slot, price, rarity:'myth', minLv}`。**已验证 `rarity` 字段在全文仅出现在数据定义、无任何渲染读取点**，故新增 `'myth'` 档**不需要改 CSS/JS** |
| A2 | `PET_HATS` / `PET_SCARFS` / `PET_WINGS`（:3307 / :3352 / :3389） | 各追加 4 个分支，代码见 §4 |
| A3 | `petBgSVG`（:3580） | 追加 4 个 `else if` 分支，代码见 §4。**注意 `<defs>` 内的 id（`skyG` `seaG` `seaGlow` `caveG` `caveGlow` `auroG` `auroGlow`）需全局唯一**——若同页渲染多个背景（商店卡会同时渲染全部 44 张预览），会重复 id。既有 `spG` 已有此问题且因渐变内容相同而不影响观感，建议沿用现状，或后续给 `petBgSVG` 加 `pref` 参数 |
| A4 | **新增 `minLv` 门禁** | 见风险 R3 |

### 6.2 风险登记

| ID | 风险 | 严重度 | 建议处置（需 team-lead / engineering-lead 拍板） |
|---|---|---|---|
| **R1** | **缩放裁切**：`scale` 已到 Lv20 的 1.75，头顶算出来 y'=−0.75，跑出 viewBox 上沿。宠物页靠 `overflow:visible` 撑住，**商店卡 `.pc-prev{overflow:hidden}` 会被裁头** | **P0** | ① **Lv21–Lv40 的 `scale` 不再增长，固定在 1.75**（甚至回落到 1.65）。"成长感"改由装扮复杂度 + 光效承担——这正是本次 16 件高阶装扮的设计意图；<br>② `costumePreviewSVG` 里把预览等级钳到 `Math.min(lv, 8)`（scale ≤1.52），让商店卡预览永远完整、且各等级一致；<br>③ 若一定要继续放大，需同步把 viewBox 扩到 `-55 -70 230 230` |
| **R2** | **主体变白**：Lv15–Lv20 `theme` 从 `#6A5CFF` 一路变浅到 `#BCADFF`，Lv20 肚皮已是 `#FFFFFF`。若 Lv21–40 继续变浅，宠物变"白团子"，任何背景都压不住 | **P0** | 建议 Lv21–40 **停止变浅，改为"加深 + 加金"**：theme 稳定在 `#5B3FE0`–`#7A4FE0` 区间、`hi` `#9E8BFF`–`#B79BFF`、`belly` 不低于 `#E8E2FF`、`line` 保持 `#FFD86B`/`#FFE08A`。**请把 Lv21–40 的 PET_STAGES 配色发我复核**，我会逐张背景做对比校验 |
| **R3** | **等级门禁缺失**：`costumeUnlockLv(slot)` 只按槽位返回 `wing:3 / hat·scarf:2 / bg:1`，无 per-item 门槛 → "Lv21–40 装扮"在 Lv2 就能买 | **P1** | ① 在 `PET_COSTUME_LIST` 加 `minLv` 字段（本规格 §0 已给每件的 Lv）；<br>② `costumeUnlockLv` 改为读 item 的 `minLv`；<br>③ 商店渲染对未达级的 card 显示 `pc-locked` + 锁图标（已有 `.pc-lockmask`） |
| **R4** | **帽子与 Lv5+ 王冠重叠**：Lv5+ 内建 `pk-crown` 占 y 29–50、x±14，与本规格帽区完全重叠，且**王冠在帽子之后绘制 → 会盖住帽子** | **P1** | 本规格的 4 顶新帽均为"冠冕类"，建议 **戴冠冕类帽子时跳过内建 `pk-crown`**：<br>`if(lv>=5 && !(wears.hat && PET_HAT_CROWN[wears.hat])) { ...crown... }`<br>`PET_HAT_CROWN = {aurora:1, gemcrown:1, mooncrown:1, phoenix:1}` |
| **R5** | **帽子与环绕字灵冲突**：Lv5+ 5 颗字灵绕 (60,86) 半径 58 旋转，扫过 y 21–35；本规格帽峰顶 y=30–32，会被穿过 | **P2** | 可接受（字灵是移动前景，短暂交叠读起来像"绕着头转"）。若要求更干净：戴冠冕帽时把字灵组的内层 `translate(60,28)` 改为 `translate(60,20)`（轨道半径 58→66，字灵底部到 y=27，与帽峰顶留 3 单位间隙） |
| **R6** | 宠物在深色背景（星海/极光）上的可辨识度依赖金色描边；若未来把 `line` 改成浅色会失效 | **P2** | 约束：Lv21–40 的 `line` 恒为金/琥珀系（`#FFD86B` 及以上），不得转白或转浅紫 |

---

## 7. 验收清单（工程实现后逐条过）

- [ ] 44 件装扮在商店内全部可见、无重复 id
- [ ] 16 件新装扮**逐件装备后能正常渲染**（帽子 4 / 围巾 4 / 翅膀 4 / 背景 4），无"买了戴不上"
- [ ] 16 件新装扮在 **Lv21–40 任意等级**下装备，帽子不被内建王冠盖住（R4）
- [ ] 商店卡片预览**不裁头**（R1 ②）
- [ ] 未达 `minLv` 的装扮显示锁定态（R3）
- [ ] 4 张新背景下，宠物轮廓在 **120 px / 200 px 两种显示尺寸**下均清晰可辨
- [ ] 开启 `prefers-reduced-motion: reduce` 后，新装扮无任何动画残留
- [ ] 灰度（滤镜去色）截图下，16 件的同类元素仍可互相区分（对照 §5.3 表）
- [ ] `node --check` 通过；全文无位图 / 外链 / web font
- [ ] 单文件体积增量 < 8 KB

---

## 8. 与 `design/pet-lv21-40.md`（文策渊 · 策划案）的并表方案

写完本规格后读到策划案，发现**三处需要主理人拍板的不一致**。我已在 §0–§5 交付完整的 16 件规格（按 PET-LV40-02 任务书要求 12–16 件、每类 3–4 件），本节给出并表选项。

### 8.1 冲突点

| 维度 | 策划案（文策渊） | 本规格（林绘澄） | 冲突性质 |
|---|---|---|---|
| 件数 | **8 件**（scarf×2 / wing×2 / hat×2 / bg×2） | **16 件**（每类 4 件） | 量级差 2 倍 |
| 获取机制 | **不走 ⭐ 购买，等级达标自动 grant** | 沿用商店 ⭐ 购买（价格 30/40/50/60⭐） | 机制互斥 |
| 命名 | 星尘围巾 / 幻彩翅膀 / 云海天宫 / 星辰法冠 / 金焰披风 / 至尊金翼 / 昊天金阙 / 至尊圣冠 | 极光冠 / 宝石冠 / 月牙冠 / 凤羽冠 / 星云围巾 / 珍珠围巾 / 缎带围巾 / 云绒围巾 / 极光翅膀 / 天使之翼 / 水晶翅膀 / 祥龙翅膀 / 云上花园 / 星海背景 / 宝石洞窟 / 极光天幕 | 无 id 冲突，**但有语义撞车**（星尘 vs 星云） |
| 等级锚点 | 22 / 26 / 30 / 32 / 34 / 38 / 40 / 40 | 22–25 / 27–30 / 32–35 / 37–40（每级最多 1 件） | 可对齐 |
| 新槽位 | 出现「**披风**」（金焰披风，Lv34，归入 scarf 槽） | 无披风（仍为 hat/scarf/wing/bg 四槽） | 披风是**第 5 个视觉槽位**，需新增渲染层 |

### 8.2 三个方案（推荐 A）

#### 方案 A（推荐）：24 件并表 —— 8 件「等级里程碑赠送」 + 16 件「商店高阶池」
- 文策渊的 8 件 = **grant 件**，走等级自动发放，商店**过滤不显示**（他的 R9 已提此要求）
- 我的 16 件 = **shop 件**，Lv21+ 解锁购买资格，价格 30/40/50/60⭐
- 好处：里程碑有"白送的惊喜"，中间档位有"攒星购买"的目标感；美术总量 24 件（约 8 KB 增量）仍可接受
- 需做：**命名去重** —— 我的 `nebula` 中文名「星云围巾」与「星尘围巾」语义撞车，建议改为 **「银河围巾」**（形状不变）
- 需做：披风按文策渊要求**新增第 5 槽位**（见 8.3）

#### 方案 B：只做 8 件（砍量，采用文策渊的名字 + 我的形状）
从我的 16 件里挑 8 件直接实现，中文名采用策划案的：

| Lv | 策划案名 | 采用我的 id | 需要的形状改造 |
|---|---|---|---|
| 22 | 星尘围巾 (scarf) | `nebula` | 无，直接复用 |
| 26 | 幻彩翅膀 (wing) | `aurorawing` | 无，直接复用 |
| 30 | 云海天宫 (bg) | `skygarden` | **需加"天宫"元素**：顶部加 1 座小宫殿剪影（3 个尖顶 + 云托），位置 y<50 |
| 32 | 星辰法冠 (hat) | `gemcrown` | **需加"星辰"元素**：5 颗宝石改为 3 宝石 + 2 四角星 |
| 34 | 金焰披风 (scarf) | **无对应** | 需新设计（见 8.3） |
| 38 | 至尊金翼 (wing) | `angelwing` | 无（羽色已是金白，天然匹配"至尊金翼"） |
| 40 | 昊天金阙 (bg) | `aurorabg` | **需加"金阙"元素**：底部雪坡改为金色宫殿台基 + 2 根柱子 |
| 40 | 至尊圣冠 (hat) | `phoenix` | 无（凤羽本就是至尊向） |

方案 B 的美术改造量：3 件需改（天宫 / 法冠 / 金阙）+ 1 件新设计（披风）≈ **1.5 天**。

#### 方案 C：只做 16 件（用我的表覆盖策划案 §4 奖励表）
- 用 §0 的 16 件替换策划案的 8 件，等级锚点改为 22/23/24/25/27/28/29/30/32/33/34/35/37/38/39/40
- **奖励密度反而更好**：最长美术空窗从 4 级降到 **2 级**（Lv25→27、Lv30→32、Lv35→37）
- 但需策划重新确认经济模型（我的 16 件合计 720⭐ 流入商店）

### 8.3 披风（cape）槽位 —— 若采用需新增渲染层
「金焰披风」在现有 4 槽里没有对应，且**披风必须在身体之后、翅膀之前绘制**（披在背上、被翅膀压住）：

```
当前绘制顺序：... → wings → body → face → hat → scarf → crown → satellites
披风插入点：     ... → wings → **cape** → body → face → ...
```
- 锚点建议：`translate(60,66)`（肩背处）
- 安全盒（局部）：`x ∈ [-34,+34]`，`y ∈ [-6,+58]` → 绝对 `x ∈ [26,94]`，`y ∈ [60,124]`
- 与围巾的冲突：披风下摆会盖住围巾区域（y 104–124）。建议 **披风与围巾互斥**（装备披风时自动卸下围巾），或把披风下摆收到 y ≤ 100

> 我**没有**在本规格里设计披风，因为它不在 PET-LV40-02 的任务范围内（任务书只列 hat/scarf/wing/bg）。若 team-lead 采纳方案 A/B，请单开一条任务给我，我会补披风规格。

---

*规格版本 v1.0 ｜ 林绘澄（美术与视觉表现指导） ｜ 待 team-lead / engineering-lead 就 R1–R6 与 §8 并表方案拍板后进入实现*
