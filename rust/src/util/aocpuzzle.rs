use std::fs;
use std::path::Path;
use std::str::Lines;
use std::time::Instant;
use colored::Colorize;
use crate::util::input::download_input;

const WIDTH: usize = 80;

pub struct Test {
    pub expected: usize,
    pub input: String
}

impl Test {
    pub fn new(expected: usize, input: String) -> Self {
        Self {
            expected,
            input: input.trim().to_string(),
        }
    }
}

pub trait AOCPuzzle<const DAY: usize, const YEAR: usize> {
    fn part1_tests(&self) -> Vec<Test>;
    fn part1(&self, input: Lines<'_>) -> usize;

    fn part2_tests(&self) -> Vec<Test>;
    fn part2(&self, input: Lines<'_>) -> usize;
}

pub struct AOCPuzzleRunner<P: AOCPuzzle<DAY, YEAR>, const DAY: usize, const YEAR: usize> {
    pub puzzle: P,
    input: String
}

impl<P: AOCPuzzle<DAY, YEAR>, const DAY: usize, const YEAR: usize> AOCPuzzleRunner<P, DAY, YEAR> {
    pub fn new(puzzle: P) -> Self {
        let filename = format!("../resources/aoc{}/Day{:02}.txt", YEAR, DAY);
        let path = Path::new(&filename);
        if !path.exists() {
            download_input(YEAR.try_into().unwrap(), DAY as i32);
        }
        let input = fs::read_to_string(path).unwrap();
        Self {
            puzzle,
            input,
        }
    }

    pub fn test_part1(&self) -> bool {
        self.test_all(&self.puzzle.part1_tests(), "Part 1 Test", |this, input| this.puzzle.part1(input))
    }

    pub fn test_part2(&self) -> bool {
        self.test_all(&self.puzzle.part2_tests(), "Part 2 Test", |this, input| this.puzzle.part2(input))
    }

    fn test_all(&self, tests: &Vec<Test>, name: &str, action: fn(&Self, Lines<'_>) -> usize) -> bool {
        if tests.len() == 1 {
            self.render_test(name, tests.first().unwrap(), action)
        } else  {
            tests.iter().enumerate().fold(true, move |value, (index, test)| {
                let name = format!("{} {}", name, index + 1);
                self.render_test(&name, test, action) && value
            })
        }
    }

    pub fn run_part1(&self) -> usize {
        self.render("Part 1", |this, input| this.puzzle.part1(input))
    }

    pub fn run_part2(&self) -> usize {
        self.render("Part 2", |this, input| this.puzzle.part2(input))
    }

    fn render(&self, name: &str, action: fn(&Self, Lines<'_>) -> usize) -> usize {
        println!("{}", self.labeled_horizontal_rule('=', name).cyan());

        self.render_timed(|| action(self, self.input.lines()))
    }

    fn render_test(&self, name: &str, test: &Test, action: fn(&Self, Lines<'_>) -> usize) -> bool {
        println!("{}", self.labeled_horizontal_rule('-', name).yellow());

        let result = action(self, test.input.lines());
        let passed = result == test.expected;
        if passed {
            println!("{}", "Passed".bright_green().bold())
        } else {
            println!("{:<width1$}{:>width1$}{:<width2$}", "Failed".red().bold(), "actual: ".red(), format!("{}", result).red().bold(), width1 = WIDTH / 4, width2 = WIDTH / 2);
            println!("{:>width$}{:<width$}", "expected: ".red(), test.expected.to_string().red().bold(), width = WIDTH / 2);
        }
        passed
    }

    fn render_timed(&self, action: impl Fn() -> usize) -> usize {
        let start = Instant::now();
        let result = action();
        let duration = start.elapsed();

        println!("{:<width$}{:>width$?}", result.to_string().bold(), duration, width = WIDTH / 2);

        result
    }

    fn horizontal_rule(&self, rule_char: char) -> String {
        rule_char.to_string().repeat(WIDTH)
    }

    fn labeled_horizontal_rule(&self, rule_char: char, label: &str) -> String {
        let rule = self.horizontal_rule(rule_char);
        format!("{}{}{}", &rule[0..(WIDTH - label.len()) / 2], label, &rule[(WIDTH + label.len()) / 2..WIDTH])
    }
}