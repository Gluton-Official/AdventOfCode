use std::ops::{Add, Mul};
use std::str::{Chars, Lines};
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day06;

impl Day06 {
    fn parse_part1(input: Lines<'_>) -> Vec<(char, Vec<usize>)>  {
        let num_problems = input.clone().next().unwrap().split_whitespace().count();
        let lines = input.clone().count();
        let problems = vec![(None::<char>, Vec::with_capacity(lines - 1)); num_problems];
        let problems: Vec<(Option<char>, Vec<usize>)> = input.fold(problems, |mut problems, line| {
            for (problem_index, value) in line.split_whitespace().enumerate() {
                match value {
                    "+" | "*" => {
                        let (_, values) = &problems[problem_index];
                        problems[problem_index] = (Some(value.chars().next().unwrap()), values.clone());
                    }
                    _ => {
                        problems[problem_index].1.push(value.parse::<usize>().unwrap());
                    }
                }
            }
            problems
        });
        problems.iter().map(|(operator, numbers)| {
            (operator.unwrap(), numbers.clone())
        }).collect()
    }

    fn parse_part2(input: Lines<'_>) -> Vec<(char, Vec<usize>)>  {
        let mut lines = input.map(|line| line.chars()).collect::<Vec<Chars<'_>>>();
        let num_digits = lines.len() - 1;
        let mut problems: Vec<(char, Vec<Vec<usize>>)> = Vec::new();
        let mut number_index = 0;
        let mut eols = 0;
        'columns: loop {
            // chars in column
            for (line_index, line) in lines.iter_mut().rev().enumerate() {
                let char = line.next();
                if char.is_none() {
                    eols += 1;
                }
                let char = char.unwrap_or(' ');
                match (line_index, char) {
                    (0, operator) => {
                        if operator != ' ' {
                            problems.push((char, vec![])); // new vector for numbers with operation

                            number_index = 0;
                        }
                        let (_, values) = problems.last_mut().unwrap();
                        values.push(Vec::with_capacity(num_digits)); // new vector for digits of number
                    }
                    (_, ' ') => {}
                    (_, digit) => {
                        let (_, values) = problems.last_mut().unwrap();
                        values[number_index].push(digit.to_digit(10).unwrap() as usize);
                    }
                }
            }

            let (_, values) = problems.last_mut().unwrap();
            if values.last().unwrap().is_empty() {
                values.pop();
            }

            if eols == lines.len() {
                break 'columns;
            }

            number_index += 1;
        }

        problems.iter().map(|&(operator, ref numbers)| {
            let numbers = numbers.iter().map(|digits| {
                let num_digits = digits.len();
                digits.iter().rev().enumerate().fold(0_usize, |acc, (index, digit)| {
                    acc + digit * (10_u64.pow((num_digits - index - 1) as u32) as usize)
                })
            }).collect::<Vec<usize>>();
            (operator, numbers)
        }).collect()
    }

    fn evaluate_problems(problems: Vec<(char, Vec<usize>)>) -> usize {
        problems.iter().map(|(operator, values)| {
            match operator {
                '+' => values.iter().fold(0, usize::add),
                '*' => values.iter().fold(1, usize::mul),
                _ => panic!()
            }
        }).sum()
    }
}

impl AOCPuzzle<06, 2025> for Day06 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 4277556,
                input: indoc! {"
                    123 328  51 64
                     45 64  387 23
                      6 98  215 314
                    *   +   *   +
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let problems = Self::parse_part1(input);
        Self::evaluate_problems(problems)
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 3263827,
                input: indoc! {"
                    123 328  51 64
                     45 64  387 23
                      6 98  215 314
                    *   +   *   +
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let problems = Self::parse_part2(input);
        Self::evaluate_problems(problems)
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day06 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            // runner.run_part1();
            assert_eq!(5782351442566, runner.run_part1());
        } else {
        }
    }

    #[test]
    fn test_part2() {
        let day = Day06 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            // runner.run_part2();
            assert_eq!(10194584711842, runner.run_part2());
        }
    }
}