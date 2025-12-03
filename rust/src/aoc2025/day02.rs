use std::collections::HashSet;
use std::ops::RangeInclusive;
use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day02;

impl Day02 {
    fn parse(input: Lines<'_>) -> Vec<RangeInclusive<usize>> {
        let line = input.into_iter().next().unwrap();
        line.split(',').map(|range| {
            let mut range = range.split('-');
            let start = range.next().unwrap().parse::<usize>().unwrap();
            let end = range.next().unwrap().parse::<usize>().unwrap();
            start..=end
        }).collect::<Vec<_>>()
    }
    fn find_invalid_ids(input: Lines<'_>) -> usize {
        let ranges = Self::parse(input);
        ranges.iter().fold(0, |acc, range| {
            let mut invalid_ids = HashSet::new();
            for id in range.clone() {
                let id_string = id.to_string();
                let id_length = id_string.len();
                let factors = (1..id_length).filter(|factor| id_length % factor == 0).collect::<Vec<_>>();
                for factor in factors {
                    let char_vec = id_string.chars().collect::<Vec<_>>();
                    let mut sequences = char_vec.chunks(factor).map(|chars| chars.iter().collect::<String>());
                    let first_seq = sequences.next().unwrap();
                    if sequences.all(|seq| seq.eq(&first_seq)) {
                        invalid_ids.insert(id);
                    }
                }
            }
            acc + invalid_ids.iter().sum::<usize>()
        })
    }
    fn find_invalid_ids_with_count(input: Lines<'_>, repeat_count: usize) -> usize {
        let ranges = Self::parse(input);
        ranges.iter().fold(0, |acc, range| {
            let mut invalid_ids = vec![];
            for id in range.clone() {
                let id_string = id.to_string();
                // can be split?
                if id_string.len() % repeat_count == 0 {
                    let (first_half, second_half) = id_string.split_at(id_string.len() / repeat_count);
                    if first_half == second_half {
                        invalid_ids.push(id);
                    }
                }
            }
            acc + invalid_ids.iter().sum::<usize>()
        })
    }
}

impl AOCPuzzle<02, 2025> for Day02 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 1227775554,
                input: indoc! {"
                    11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        Self::find_invalid_ids_with_count(input, 2)
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 4174379265,
                input: indoc! {"
                    11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        Self::find_invalid_ids(input)
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day02 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            assert_eq!(38437576669, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day02 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            assert_eq!(49046150754, runner.run_part2());
        }
    }
}