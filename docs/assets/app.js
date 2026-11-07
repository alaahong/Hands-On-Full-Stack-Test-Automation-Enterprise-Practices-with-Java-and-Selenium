(function () {
  "use strict";

  const data = window.SITE_INDEX;
  const lang = (document.documentElement.lang || "zh").toLowerCase().startsWith("en") ? "en" : "zh";
  const state = {
    query: "",
    filter: "all",
  };

  const UI = {
    zh: {
      moduleLabel: "模块",
      copy: "复制链接",
      copied: "已复制",
      all: "全部条目",
      allTitle: "显示全部条目",
      external: "外部与本地站点",
      statsSites: "个站内页面",
      statsExternal: "个外部 / 本地站点",
      statsChapters: "个章节",
      entryCount: (n) => n + " 个条目",
      variantsLabel: "可选地址：",
      localBadge: "本地",
      resultAll: (total) => "共 " + total + " 个条目",
      resultFiltered: (visible, total) => "显示 " + visible + " / " + total + " 个条目",
    },
    en: {
      moduleLabel: "Module",
      copy: "Copy link",
      copied: "Copied",
      all: "All entries",
      allTitle: "Show all entries",
      external: "External and local",
      statsSites: "site pages",
      statsExternal: "external / local sites",
      statsChapters: "chapters",
      entryCount: (n) => n + (n === 1 ? " entry" : " entries"),
      variantsLabel: "Variants:",
      localBadge: "Local",
      resultAll: (total) => "Showing all " + total + " entries",
      resultFiltered: (visible, total) => "Showing " + visible + " of " + total + " entries",
    },
  }[lang];

  const EXTERNAL_ICON =
    '<svg aria-hidden="true" viewBox="0 0 24 24"><path d="M14 3h7v7m0-7L10 14M5 5h5M5 5v14h14v-5"/></svg>';
  const COPY_ICON =
    '<svg aria-hidden="true" viewBox="0 0 24 24"><rect x="9" y="9" width="11" height="11" rx="2"/><path d="M5 15V5a2 2 0 0 1 2-2h10"/></svg>';
  const CHECK_ICON =
    '<svg aria-hidden="true" viewBox="0 0 24 24"><path d="m5 12 4.5 4.5L19 7"/></svg>';

  const $ = (selector) => document.querySelector(selector);

  function t(value) {
    if (value && typeof value === "object") {
      return value[lang] || value.zh || value.en || "";
    }
    return value == null ? "" : String(value);
  }

  function esc(value) {
    return String(value)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
  }

  function normalize(value) {
    return String(value).toLowerCase().replace(/\s+/g, " ").trim();
  }

  function collectText(value, parts) {
    if (value == null) return;
    if (typeof value === "object") {
      Object.keys(value).forEach((key) => collectText(value[key], parts));
      return;
    }
    parts.push(String(value));
  }

  function searchText(site) {
    const parts = [];
    collectText(site.name, parts);
    collectText(site.path, parts);
    collectText(site.desc, parts);
    collectText(site.note, parts);
    collectText(site.refs, parts);
    (site.variants || []).forEach((variant) => collectText(variant.label, parts));
    return normalize(parts.join(" "));
  }

  function shortTitle(title) {
    const parts = t(title).split("·");
    return (parts.length > 1 ? parts[parts.length - 1] : parts[0]).trim();
  }

  function copyButton(url) {
    if (!url) return "";
    return (
      '<button class="copy" type="button" data-url="' + esc(url) +
      '" aria-label="' + esc(UI.copy) + '" title="' + esc(UI.copy) + '">' + COPY_ICON + "</button>"
    );
  }

  function siteCard(site) {
    const variants = (site.variants || []).filter((v) => v.url !== site.url);
    const variantHtml = variants.length
      ? '<div class="variants"><span class="variants-label">' + esc(UI.variantsLabel) + "</span>" +
        variants
          .map(
            (v) =>
              '<a class="variant" href="' + esc(v.url) +
              '" target="_blank" rel="noopener">' + esc(t(v.label)) + "</a>"
          )
          .join("") +
        "</div>"
      : "";

    const name = t(site.name);
    const nameHtml = site.hideLink
      ? '<span class="entry-name">' + esc(name) + "</span>"
      : '<a class="entry-name" href="' + esc(site.url) + '" target="_blank" rel="noopener">' +
        esc(name) + EXTERNAL_ICON + "</a>";

    const localBadge = site.local
      ? '<span class="badge-local">' + esc(UI.localBadge) + "</span>"
      : "";
    const note = t(site.note);
    const noteHtml = note ? '<p class="entry-note">' + esc(note) + "</p>" : "";
    const refsHtml = (site.refs || [])
      .map((r) => '<span class="ref">' + esc(t(r)) + "</span>")
      .join("");

    return (
      '<article class="entry' + (site.local ? " local" : "") +
      '" data-search="' + esc(searchText(site)) + '">' +
      '<div class="entry-body">' +
      '<div class="entry-title-row">' + nameHtml + localBadge + "</div>" +
      '<code class="entry-path">' + esc(site.path) + "</code>" +
      '<p class="entry-desc">' + esc(t(site.desc)) + "</p>" +
      noteHtml + variantHtml +
      '<div class="refs">' + refsHtml + "</div>" +
      "</div>" +
      copyButton(site.url) +
      "</article>"
    );
  }

  function chapterSection(chapter) {
    return (
      '<section class="chapter" id="' + esc(chapter.id) + '" data-filter="' + esc(chapter.id) + '">' +
      '<div class="chapter-head">' +
      '<div class="chapter-title-row">' +
      '<h2 class="chapter-title">' + esc(t(chapter.title)) + "</h2>" +
      '<span class="chapter-count">' + esc(UI.entryCount(chapter.sites.length)) + "</span>" +
      "</div>" +
      '<p class="chapter-meta">' + esc(UI.moduleLabel) + " <code>" + esc(chapter.module) + "</code></p>" +
      '<p class="chapter-summary">' + esc(t(chapter.summary)) + "</p>" +
      "</div>" +
      '<div class="entries">' + chapter.sites.map(siteCard).join("") + "</div>" +
      "</section>"
    );
  }

  function externalSection() {
    const sites = data.external.map((site) =>
      siteCard({
        name: site.name,
        path: site.host,
        url: site.url,
        desc: site.desc,
        refs: site.refs,
        note: site.note,
        local: site.local,
        hideLink: site.hideLink,
      })
    );
    return (
      '<section class="chapter" id="external" data-filter="external">' +
      '<div class="chapter-head">' +
      '<div class="chapter-title-row">' +
      '<h2 class="chapter-title">' + esc(t(data.externalTitle)) + "</h2>" +
      '<span class="chapter-count">' + esc(UI.entryCount(data.external.length)) + "</span>" +
      "</div>" +
      '<p class="chapter-summary">' + esc(t(data.externalSummary)) + "</p>" +
      "</div>" +
      '<div class="entries">' + sites.join("") + "</div>" +
      "</section>"
    );
  }

  function render() {
    $("#content").innerHTML =
      data.chapters.map(chapterSection).join("") + externalSection();
    $("#updated").textContent = data.meta.updated;
    renderStats();
    renderFilters();
  }

  function renderStats() {
    const total = data.chapters.reduce((sum, c) => sum + c.sites.length, 0);
    $("#stats").innerHTML =
      "<span><strong>" + total + "</strong> " + esc(UI.statsSites) + "</span>" +
      "<span><strong>" + data.external.length + "</strong> " + esc(UI.statsExternal) + "</span>" +
      "<span><strong>" + data.chapters.length + "</strong> " + esc(UI.statsChapters) + "</span>";
  }

  function renderFilters() {
    const total = data.chapters.reduce((sum, c) => sum + c.sites.length, 0);
    const filters = [{ id: "all", code: "ALL", name: UI.all, count: total, title: UI.allTitle }]
      .concat(
        data.chapters.map((c) => ({
          id: c.id,
          code: c.code,
          name: shortTitle(c.title),
          count: c.sites.length,
          title: t(c.title),
        }))
      )
      .concat([
        {
          id: "external",
          code: "EX",
          name: UI.external,
          count: data.external.length,
          title: t(data.externalTitle),
        },
      ]);

    $("#chapter-filter").innerHTML = filters
      .map(
        (f) =>
          '<button class="chip" type="button" data-filter="' + esc(f.id) +
          '" title="' + esc(f.title) + '" aria-pressed="' + (state.filter === f.id) + '">' +
          '<span class="chip-code">' + esc(f.code) + "</span>" +
          '<span class="chip-name">' + esc(f.name) + "</span>" +
          '<span class="chip-count">' + f.count + "</span>" +
          "</button>"
      )
      .join("");
  }

  function apply() {
    const query = normalize(state.query);
    let visible = 0;
    const total = document.querySelectorAll(".entry").length;

    document.querySelectorAll(".chapter").forEach((section) => {
      const filterMatch = state.filter === "all" || section.dataset.filter === state.filter;
      let sectionVisible = 0;

      section.querySelectorAll(".entry").forEach((entry) => {
        const matches = filterMatch && (!query || entry.dataset.search.includes(query));
        entry.hidden = !matches;
        if (matches) {
          visible++;
          sectionVisible++;
        }
      });

      section.hidden = sectionVisible === 0;
    });

    document.querySelectorAll(".chip").forEach((chip) => {
      chip.setAttribute("aria-pressed", String(chip.dataset.filter === state.filter));
    });

    $("#empty").hidden = visible !== 0;
    $("#result-line").textContent = visible === total
      ? UI.resultAll(total)
      : UI.resultFiltered(visible, total);
  }

  function scrollToFilter(filter) {
    const target = filter === "all" ? $(".main-toolbar") : document.getElementById(filter);
    if (!target) return;
    target.scrollIntoView({ block: "start" });
  }

  function setFilter(filter, updateHash) {
    state.filter = filter;
    apply();
    if (updateHash) {
      const hash = filter === "all" ? "" : "#" + filter;
      history.replaceState(null, "", hash || location.pathname + location.search);
      scrollToFilter(filter);
    }
  }

  function bind() {
    $("#search").addEventListener("input", (event) => {
      state.query = event.target.value;
      apply();
    });

    $("#chapter-filter").addEventListener("click", (event) => {
      const chip = event.target.closest(".chip");
      if (!chip) return;
      setFilter(chip.dataset.filter, true);
    });

    $("#content").addEventListener("click", async (event) => {
      const button = event.target.closest(".copy");
      if (!button) return;
      const url = button.dataset.url;
      try {
        await navigator.clipboard.writeText(url);
      } catch (error) {
        const area = document.createElement("textarea");
        area.value = url;
        area.style.position = "fixed";
        area.style.opacity = "0";
        document.body.appendChild(area);
        area.select();
        document.execCommand("copy");
        area.remove();
      }
      button.classList.add("done");
      button.innerHTML = CHECK_ICON;
      button.setAttribute("aria-label", UI.copied);
      setTimeout(() => {
        button.classList.remove("done");
        button.innerHTML = COPY_ICON;
        button.setAttribute("aria-label", UI.copy);
      }, 1200);
    });

    document.addEventListener("keydown", (event) => {
      const tag = (event.target.tagName || "").toLowerCase();
      const typing = tag === "input" || tag === "textarea" || event.target.isContentEditable;
      if (event.key === "/" && !typing) {
        event.preventDefault();
        $("#search").focus();
      }
      if (event.key === "Escape" && document.activeElement === $("#search")) {
        $("#search").value = "";
        state.query = "";
        apply();
        $("#search").blur();
      }
    });

    const hash = location.hash.replace("#", "");
    if (hash && (hash === "external" || data.chapters.some((c) => c.id === hash))) {
      state.filter = hash;
    }
  }

  render();
  bind();
  apply();
  if (state.filter !== "all") scrollToFilter(state.filter);
})();
