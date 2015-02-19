def gen_opt_set(num, prob)
    (0...num).to_a.select{|i| rand < prob}.map{|val| (val + 97).chr}.join
end

def gen_non_empty_opt_set(num, prob)
    until ((set = gen_opt_set(num, prob)) != "")
    end
    set
end

def gen_uniq_sets(num, max, prob)
    sets = (0...max).to_a.map{|i| gen_non_empty_opt_set(num, prob)}.uniq
    "#{sets.length} #{sets.join(" ")}"
end

def gen_test_case(num, max, prob)
    "#{num}\n#{(0...num).to_a.map{|i| gen_uniq_sets(num, max, prob)}.join("\n")}"
end

def write_case(casenum, num, max, prob)
    f = File.new "../inputs/GameStrategy.in#{casenum}", "w"
    f.puts gen_test_case(num, max, prob)
    f.close
end
