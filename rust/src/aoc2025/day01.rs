use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day01;

impl AOCPuzzle<1, 2025> for Day01 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 3,
                input: indoc! {"
                    L68
                    L30
                    R48
                    L5
                    R60
                    L55
                    L1
                    L99
                    R14
                    L82
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let mut zeros = 0;
        let distance: i32 = input
            .fold(50, |acc, line| {
                let dir = line.chars().next().unwrap();
                let dis = line.get(1..).unwrap().parse::<i32>().unwrap();
                let dis = match dir {
                    'L' => -dis,
                    'R' => dis,
                    _ => todo!()
                };
                let new = ((acc + dis) + 100) % 100;
                if new == 0 { zeros += 1; }
                new
            });
        zeros
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 6,
                input: indoc! {"
                    L68
                    L30
                    R48
                    L5
                    R60
                    L55
                    L1
                    L99
                    R14
                    L82
                "}.trim().into()
            },
            Test {
                expected: 15,
                input: indoc! {"
                    L400
                    R400
                    L250
                    R300
                    R50
                    R50
                    L30
                    L30
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let mut zeros = 0;
        let distance: i32 = input
             .fold(50, |acc, line| {
                 let dir = line.chars().next().unwrap();
                 let dis = line.get(1..).unwrap().parse::<i32>().unwrap();
                 let dis = match dir {
                     'L' => -dis,
                     'R' => dis,
                     _ => todo!()
                 };
                 let mut rotations = if dis.abs() > 100 {
                     (dis / 100).abs() as usize
                 } else {
                     0
                 };
                 let new_dis = dis % 100;
                 let raw = acc + new_dis;
                 if (new_dis < 0 && raw < 0 && raw > new_dis) || raw > 100 { rotations += 1; }
                 let new = ((raw % 100) + 100) % 100;
                 if new == 0 { rotations += 1; }
                 if acc == 0 && new == 0 && dis != 0 { rotations -= 1; }
                 if dis.abs() > 100 {
                     println!("{}--({})->{} ({})", acc, dis, new, rotations);
                 }
                 zeros += rotations;
                 new
             });
        zeros
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;
    
    #[test]
    fn test_part1() {
        let day = Day01 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            runner.run_part1();
        }
    }

    #[test]
    fn test_part2() {
        let day = Day01 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            runner.run_part2();
        }
    }
}