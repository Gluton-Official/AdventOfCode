use chrono::{Datelike, Local, NaiveDate};
use colored::Colorize;
use dotenv::dotenv;
use reqwest::blocking::Client;
use reqwest::header::COOKIE;
use std::fs;
use std::path::Path;

pub fn download_input_today() {
    let today = Local::now().date_naive();
    download_input(today.year(), today.day() as i32)
}
pub fn download_input(year: i32, day: i32) {
    assert!((1..25).contains(&day));
    assert!(year >= 2015);

    dotenv().unwrap();

    let target_date = NaiveDate::from_ymd_opt(year, 12, day as u32).unwrap();
    assert!(target_date <= Local::now().date_naive());

    let input_filename = format!("../resources/aoc{}/Day{:02}.txt", year, day);
    let input_path = Path::new(&input_filename);
    if input_path.exists() {
        println!("Input already downloaded: {}", input_path.display());
        return;
    }

    let session = std::env::var("session").expect("Session token not found in .env");
    let client = Client::new();
    let url = format!("https://adventofcode.com/{}/day/{}/input", year, day);

    let msg = format!("Downloading {}...", url).bright_black();
    println!("{}", msg);

    let response = client
        .get(&url)
        .header(COOKIE, format!("session={}", session))
        .send()
        .unwrap();

    assert!(response.status().is_success(), "Failed downloading {}: {}", url, response.status());

    let text = response.text().unwrap();

    if let Some(year_dir) = input_path.parent() {
        fs::create_dir_all(year_dir).unwrap();
    }
    fs::write(input_path, text).unwrap();

    let msg = format!("Downloaded to {}", input_path.display());
    println!("{}", msg.blue());
}