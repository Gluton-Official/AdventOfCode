use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day04;

impl Day04 {
    fn parse(input: Lines<'_>) -> Vec<Vec<bool>> {
        input.map(|line| line.chars().map(|c| c == '@').collect()).collect()
    }

    fn accessible(paper_rolls: &Vec<Vec<bool>>, position: (usize, usize)) -> bool {
        let adjacents: Vec<(i32, i32)> = vec![
            (-1, -1),
            (-1, 0),
            (-1, 1),
            (0, -1),
            (0, 1),
            (1, -1),
            (1, 0),
            (1, 1),
        ];
        let adjacent_rolls = adjacents.iter()
            .map(|&(row, column)| (position.0 as i32 + row, position.1 as i32 + column))
            .filter(|&position| Self::has_paper(paper_rolls, position))
            .count();

        adjacent_rolls < 4
    }

    fn has_paper(paper_rolls: &Vec<Vec<bool>>, position: (i32, i32)) -> bool {
        if position.0 < 0 || position.1 < 0 {
            false
        } else {
            let position = (position.0 as usize, position.1 as usize);
            *(paper_rolls.get(position.0).and_then(|row| row.get(position.1)).unwrap_or(&false))
        }
    }
}

impl AOCPuzzle<04, 2025> for Day04 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 13,
                input: indoc! {"
                    ..@@.@@@@.
                    @@@.@.@.@@
                    @@@@@.@.@@
                    @.@@@@..@.
                    @@.@@@@.@@
                    .@@@@@@@.@
                    .@.@.@.@@@
                    @.@@@.@@@@
                    .@@@@@@@@.
                    @.@.@@@.@.
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let paper_rolls = Self::parse(input);

        let mut accessible_rolls = 0;
        for (row_index, row) in paper_rolls.iter().enumerate() {
            for (column_index, &is_roll) in row.iter().enumerate() {
                if is_roll {
                    if Self::accessible(&paper_rolls, (row_index, column_index)) {
                        accessible_rolls += 1;
                    }
                }
            }
        }
        accessible_rolls
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 43,
                input: indoc! {"
                    ..@@.@@@@.
                    @@@.@.@.@@
                    @@@@@.@.@@
                    @.@@@@..@.
                    @@.@@@@.@@
                    .@@@@@@@.@
                    .@.@.@.@@@
                    @.@@@.@@@@
                    .@@@@@@@@.
                    @.@.@@@.@.
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let mut paper_rolls = Self::parse(input);

        let mut removable_rolls = 0;
        loop {
            let mut accessible_rolls = vec![];
            for (row_index, row) in paper_rolls.iter().enumerate() {
                for (column_index, &is_roll) in row.iter().enumerate() {
                    if is_roll {
                        if Self::accessible(&paper_rolls, (row_index, column_index)) {
                            accessible_rolls.push((row_index, column_index));
                        }
                    }
                }
            }
            if accessible_rolls.len() == 0 {
                break;
            }
            removable_rolls += accessible_rolls.len();
            accessible_rolls.iter().for_each(|&(row_index, column_index)| {
                paper_rolls[row_index][column_index] = false;
            })
        }
        removable_rolls
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day04 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            // runner.run_part1();
            assert_eq!(1489, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day04 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            // runner.run_part2();
            assert_eq!(8890, runner.run_part2());
        }
    }
}