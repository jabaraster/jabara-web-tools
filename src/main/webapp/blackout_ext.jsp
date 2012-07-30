<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<head>
  <title>優先度抽出HTML</title>
  <meta charset="UTF-8" />
  <script src="jquery-1.7.2.min.js"></script>
  <style>
  #scheduleText {
    width: 80%;
    height: 20em;
  }
  </style>
</head>
<body>

  <button id="scraper7">7月分のスクレイピングの開始！</button>
  <button id="scraper8">8月分のスクレイピングの開始！</button>
  <button id="scraper9">9月分のスクレイピングの開始！</button>

  <form method="post" action="${pageContext.request.contextPath}/rest/blackout/schedule.csv">
    <textarea id="scheduleText" name="scheduleText"></textarea>
    <br />
    <input type="submit" value="サーバに登録" />
  </form>
  <div id="console">
  </div>

  <script>

  var contextPath = '${pageContext.request.contextPath}';
  
  jQuery(initialize);
  function initialize() {
    $('#scraper7').click(function() {
      startScraping('/restriction/blackoutschedule');
    });
    $('#scraper8').click(function() {
      startScraping('/restriction/blackoutschedule/1');
    });
    $('#scraper9').click(function() {
      startScraping('/restriction/blackoutschedule/2');
    });
  }

  var priorityPageScraperCount = 0;

  function startScraping(pTargetPageUrlPath) {
    log("スクレイピングを開始します。");
    var calendarUrl = qdenUrl(pTargetPageUrlPath);
    $.get(calendarUrl, null, scrapeCalendarPage);
  }

  function scrapeCalendarPage(pContent) {
    $('span.cal_group > a', pContent).each(function(i, e) {
      var href = $(e).attr('href');
      log(href + 'をスクレイピング中...');
      var path = href.replace('http://www2.kyuden.co.jp/kt_search/index.php', '');
      var priorityUrl = qdenUrl(path);
      ++priorityPageScraperCount;
      $.get(priorityUrl, null, scrapePriorityPage);
      //return i < 0;
    });
  }

  function scrapePriorityPage(pContent) {
    try {
      appendDataLine($('span[style="font-size:1.2em;font-weight:bold;"]', pContent).text());
      var groups = $('div[style="border:1px solid #999999;width:350px;padding:0.3em;float:left;line-height:2.2em;"] > p > a', pContent);
      $('div[style="border:1px solid #FFFFFF;width:15%;padding:0.3em 0.5em;float:left;line-height:2.2em;"] > p', pContent).each(function(i, e) {
        appendDataLine($(e).text() + ' ' + $(groups.get(i)).text());
      });
    } finally {
      --priorityPageScraperCount;
      if (priorityPageScraperCount == 0) {
        log("スクレイプ完了！");
      }
    }
  }

  var con = $('#console');
  var dataArea = $('#scheduleText');
  function appendDataLine(pLine) {
    dataArea.append(pLine + '\n');
  }
  function log(s) {
    con.append(s + '<br/>');
  }

  function qdenUrl(pPath) {
    return contextPath + '/rest/blackout/scrape?path=' + encodeURI(pPath)
  }

  </script>

</body>
