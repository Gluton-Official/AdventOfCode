use crate::util::aocpuzzle::{AOCPuzzle, Test};

struct Day00;

impl AOCPuzzle<0, 2025> for Day00 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![]
    }
    fn part1(&self, input: String) -> usize {
        0
    }
    
    fn part2_tests(&self) -> Vec<Test> {
        vec![]
    }
    fn part2(&self, input: String) -> usize {
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
        
        if runner.test_part1() {
            runner.run_part1();
        }
    }

    #[test]
    fn test_part2() {
        let day = Day00 {};
        let runner = AOCPuzzleRunner::new(day);

        if runner.test_part2() {
            runner.run_part2();
        }
    }
}