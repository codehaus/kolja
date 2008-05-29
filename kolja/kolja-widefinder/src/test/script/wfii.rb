require 'java'

include_class 'com.baulsupp.kolja.ansi.reports.script.ScriptReport'

class WFIIArticles < ScriptReport
  def initialize
    @u_hits = {}
    @u_bytes = {}
    @s404s = {}
    @clients = {}
    @refs = {}
  
    @u_hits.default = @u_bytes.default = @s404s.default = @clients.default = @refs.default = 0
  end
  
  def record(client, u, bytes, ref)  
    @u_bytes[u] += bytes
    if u =~  %r{^/ongoing/When/\d\d\dx/\d\d\d\d/\d\d/\d\d/[^ .]+$}
      @u_hits[u] += 1
      @clients[client] += 1
      unless (ref == nil || ref == '-' || ref =~ %r{^http://www.tbray.org/ongoing/})
        @refs[ref] += 1 
      end
    end
  end

  def report(label, hash, shrink = false)
    puts "Top #{label}:"
    keys_by_count = hash.keys.sort_by{ |key| -hash[key] }[0 .. 9]
    fmt = (shrink) ? " %9.1fM: %s\n" : " %10d: %s\n"
    keys_by_count.each do |key|
      pkey = (key.length > 60) ? key[0 .. 59] + "..." : key
      hash[key] = hash[key] / (1024.0 * 1024.0) if shrink
      printf fmt, hash[key], pkey
    end
    puts
  end

  def processLine(line)
    return unless line.action == 'GET'
  
    status = line.status.code

    if status == '200'
      record(line.ipaddress, line.url, line.size, line.referrer)
    elsif status == '304'
      record(line.ipaddress, line.url, 0, line.referrer)
    elsif status == '404'
      @s404s[line.url] += 1
    end
  end

  def completed
    report('URIs by hit', @u_hits)
    report('URIs by bytes', @u_bytes, true)
    report('404s', @s404s)
    report('client addresses', @clients)
    report('referrers', @refs)
  end
end

WFIIArticles.new