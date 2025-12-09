use std::collections::{HashMap, HashSet};
use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day07;

impl Day07 {}

impl AOCPuzzle<07, 2025> for Day07 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 21,
                input: indoc! {"
                    .......S.......
                    ...............
                    .......^.......
                    ...............
                    ......^.^......
                    ...............
                    .....^.^.^.....
                    ...............
                    ....^.^...^....
                    ...............
                    ...^.^...^.^...
                    ...............
                    ..^...^.....^..
                    ...............
                    .^.^.^.^.^...^.
                    ...............
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let mut manifold = input.clone();
        let start_index = manifold.next().unwrap().find('S').unwrap();
        let mut beam_indices = HashSet::<usize>::new();
        beam_indices.insert(start_index);
        manifold.skip(1).fold(0_usize, |mut splits, row| {
            let mut new_beam_indices = HashSet::<usize>::new();
            for (column_index, byte) in row.bytes().enumerate() {
                if byte as char == '^' && beam_indices.contains(&column_index) {
                    beam_indices.remove(&column_index);
                    new_beam_indices.insert(column_index - 1);
                    new_beam_indices.insert(column_index + 1);
                    splits += 1;
                }
            }
            beam_indices.iter().for_each(|&index| { new_beam_indices.insert(index); });
            // println!("{}", row.bytes().enumerate().map(|(index, byte)| {
            //     if new_beam_indices.contains(&index) {
            //         '|'
            //     } else {
            //         byte as char
            //     }
            // }).collect::<String>());
            beam_indices = new_beam_indices;
            splits
        })
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 40,
                input: indoc! {"
                    .......S.......
                    ...............
                    .......^.......
                    ...............
                    ......^.^......
                    ...............
                    .....^.^.^.....
                    ...............
                    ....^.^...^....
                    ...............
                    ...^.^...^.^...
                    ...............
                    ..^...^.....^..
                    ...............
                    .^.^.^.^.^...^.
                    ...............
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let mut manifold = input.clone();
        let start_index = manifold.next().unwrap().find('S').unwrap();
        let mut beam_paths = HashMap::<usize, usize>::new();
        beam_paths.insert(start_index, 1);
        manifold.skip(1).for_each(|row| {
            let mut new_beam_paths = HashMap::<usize, usize>::new();
            row.bytes().enumerate()
                .filter_map(|(index, byte)| {
                    match byte as char {
                        '^' => Some(index),
                        _ => None
                    }
                })
                .for_each(|beam_splitter| {
                    let beams = beam_paths.remove(&beam_splitter).unwrap_or(0);
                    if beams != 0 {
                        let left_path = beam_splitter - 1;
                        if let Some(left_beams) = new_beam_paths.get_mut(&left_path) {
                            *left_beams += beams
                        } else {
                            new_beam_paths.insert(left_path, beams);
                        }
                        let right_path = beam_splitter + 1;
                        if let Some(right_beams) = new_beam_paths.get_mut(&right_path) {
                            *right_beams += beams
                        } else {
                            new_beam_paths.insert(right_path, beams);
                        }
                    }
                });
            new_beam_paths.iter().for_each(|(&path, &new_beams)| {
                if let Some(beams) = beam_paths.get_mut(&path) {
                    *beams += new_beams
                } else {
                    beam_paths.insert(path, new_beams);
                }
            });
        });
        beam_paths.iter().fold(0, |paths, (_, beams)| {
            paths + beams
        })
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day07 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            // runner.run_part1();
            assert_eq!(1646, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day07 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            // runner.run_part2();
            assert_eq!(32451134474991, runner.run_part2());
        }
    }
}