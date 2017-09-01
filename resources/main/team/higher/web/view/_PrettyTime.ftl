<#macro PrettyTime date>
  <a style="text-decoration: none; color: black;" href="#" onClick="event.preventDefault();" title="${date.humanString}">${date.dateString} ${date.timeString}</a>
</#macro>
