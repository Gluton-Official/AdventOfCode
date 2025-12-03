use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day00;

impl Day00 {
    
}

impl AOCPuzzle<0, 0000> for Day00 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 0,
                input: indoc! {"

                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        0
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
        0
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day00 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            runner.run_part1();
            // assert_eq!(, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day00 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            runner.run_part2();
            // assert_eq!(, runner.run_part2());
        }
    }
}