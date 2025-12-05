use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day03;

impl Day03 {
    fn parse(input: Lines<'_>) -> Vec<Vec<usize>> {
        input.map(|line| line.chars().map(|digit| digit.to_digit(10).unwrap() as usize).collect()).collect()
    }
    fn maximize_joltage_of_two(bank: &Vec<usize>) -> usize {
        let mut max_joltage = 0;
        for battery1 in 0..bank.len() {
            for battery2 in (battery1 + 1)..bank.len() {
                let joltage = bank[battery1] * 10 + bank[battery2];
                if joltage > max_joltage {
                    max_joltage = joltage;
                }
            }
        }
        max_joltage
    }

    fn maximize_joltage(bank: &Vec<usize>, battery_count: usize) -> usize {
        let mut bank = bank.iter().enumerate().map(|(index, joltage)| (*joltage, index)).collect::<Vec<_>>();

        let mut selected_batteries = Vec::<(usize, usize)>::with_capacity(battery_count);
        for _ in 0..battery_count {
            let (mut index, &battery) = bank.clone().iter().enumerate()
                .max_by(|(_, (joltage1, index1)), (_, (joltage2, index2))| {
                    joltage1.cmp(joltage2).then(index1.cmp(index2))
                }).unwrap();
            let mut battery = battery;
            if let Some(&starting_battery) = selected_batteries.iter().min_by(|(_, index1), (_, index2)| index1.cmp(index2)) {
                let (starting_battery_joltage, starting_battery_index) = starting_battery;
                let (battery_joltage, battery_index) = battery;
                if battery_index < starting_battery_index && battery_joltage < starting_battery_joltage {
                    if let Some((next_index, &next_battery)) = bank.clone().iter().enumerate().rfind(|&(_, &(joltage, index))| index > starting_battery_index && joltage <= battery_joltage) {
                        index = next_index;
                        battery = next_battery;
                    }
                }
            }
            bank.remove(index);
            selected_batteries.push(battery);
        }
        selected_batteries.sort_by(|(_, index1), (_, index2)| index1.cmp(index2));

        let selected_batteries = selected_batteries.iter().map(|(joltage, _)| *joltage).collect::<Vec<_>>();
        println!("{}", selected_batteries.iter().map(|x| x.to_string()).collect::<Vec<_>>().join(""));

        selected_batteries.iter().enumerate().fold(0, |acc, (index, joltage)| acc + joltage * 10_u64.pow((battery_count - index - 1) as u32) as usize)
    }
}

impl AOCPuzzle<03, 2025> for Day03 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 357,
                input: indoc! {"
                    987654321111111
                    811111111111119
                    234234234234278
                    818181911112111
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let banks = Self::parse(input);
        let max_bank_joltages = banks.iter().map(|bank| Self::maximize_joltage_of_two(bank)).collect::<Vec<usize>>();
        max_bank_joltages.iter().sum()
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 3121910778619,
                input: indoc! {"
                    987654321111111
                    811111111111119
                    234234234234278
                    818181911112111
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let banks = Self::parse(input);
        let max_bank_joltages = banks.iter().map(|bank| Self::maximize_joltage(bank, 12)).collect::<Vec<usize>>();
        max_bank_joltages.iter().sum()
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day03 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            assert_eq!(16854, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day03 {};
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            runner.run_part2();
            // assert_eq!(, runner.run_part2());
        }
    }
}