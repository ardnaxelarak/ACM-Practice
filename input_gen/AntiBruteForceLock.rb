def gen_test_case(min, max)
    keys = rand(max - min) + min
    "#{keys} #{(0...10000).to_a.sample(keys).map{|i| i.to_s.rjust(4, "0")}.join(" ")}"
end

def gen_test_case_set(num, min, max)
    "#{num}\n#{(0...num).to_a.map{|i| gen_test_case(min, max)}.join("\n")}"
end

def write_case(casenum, num, max, prob)
    f = File.new "../judging/brute-#{casenum}.in", "w"
    f.puts gen_test_case_set(num, max, prob)
    f.close
end
