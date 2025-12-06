use std::cmp::max;
use std::ops::RangeInclusive;
use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day05;

impl Day05 {
    fn parse(input: Lines<'_>) -> (Vec<usize>, Vec<RangeInclusive<usize>>) {
        let fresh_id_ranges = input.clone()
            .take_while(|line| !line.is_empty())
            .map(|line| {
                let [start, end] = line.split('-')
                    .map(|id| id.parse::<usize>().unwrap())
                    .collect::<Vec<usize>>()
                    .try_into().unwrap();
                start..=end
            }).collect();
        let ingredient_ids = input
            .skip_while(|line| !line.is_empty())
            .skip(1)
            .map(|line| line.parse::<usize>().unwrap())
            .collect();
        (ingredient_ids, fresh_id_ranges)
    }

    fn condense_ranges(range1: &RangeInclusive<usize>, range2: &RangeInclusive<usize>) -> (RangeInclusive<usize>, Option<RangeInclusive<usize>>) {
        if range1.contains(range2.start()) {
            // range1: |-----|      |  |---------------|
            // range2:     |-----|  |       |------|
            // result: |---------|  |  |---------------|
            (*range1.start()..=*max(range1.end(), range2.end()), None)
        } else if range2.contains(range1.end()) {
            // range2: |-----|      |  |---------------|
            // range1:     |-----|  |       |------|
            // result: |---------|  |  |---------------|
            (*range2.start()..=*max(range1.end(), range2.end()), None)
        } else {
            (range1.clone(), Some(range2.clone()))
        }
    }
}

impl AOCPuzzle<05, 2025> for Day05 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 3,
                input: indoc! {"
                    3-5
                    10-14
                    16-20
                    12-18

                    1
                    5
                    8
                    11
                    17
                    32
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let (ingredient_ids, fresh_id_ranges) = Self::parse(input);
        ingredient_ids.iter().filter(|id| fresh_id_ranges.iter().any(|range| range.contains(id))).count()
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 0,
                input: indoc! {"

                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let (_, mut fresh_id_ranges) = Self::parse(input);
        fresh_id_ranges.sort_by_key(|range| *range.start());
        let condensed_fresh_id_ranges = fresh_id_ranges.iter().fold(vec![], |mut condensed_ranges, range| {
            if condensed_ranges.is_empty() {
                return vec![range.clone()];
            }

            let last_condensed_range = condensed_ranges.last().unwrap();
            match Self::condense_ranges(last_condensed_range, range) {
                (condensed_range, None) => {
                    condensed_ranges.pop();
                    condensed_ranges.push(condensed_range);
                }
                (_, Some(_)) => condensed_ranges.push(range.clone())
            }
            condensed_ranges
        });

        condensed_fresh_id_ranges.iter().fold(0, |acc, range| acc + range.clone().count())
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day05 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            // runner.run_part1();
            assert_eq!(694, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day05 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            // runner.run_part2();
            assert_eq!(352716206375547, runner.run_part2());
        }
    }
}