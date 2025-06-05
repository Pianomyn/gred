import random
import shutil
import string
import sys
from pathlib import Path


def generate_random_text(size_in_mb):
    chars = string.ascii_letters + string.digits + " \n"
    num_chars = size_in_mb * 1024 * 1024
    return "".join(random.choices(chars, k=num_chars))


def create_structure(
    base_path, depth, width, files_per_dir, file_size_mb, current_depth=1
):
    for f in range(1, files_per_dir + 1):
        file_path = base_path / f"file_{current_depth}_{f}.txt"
        print(f"Creating file: {file_path}")
        with open(file_path, "w") as f_out:
            f_out.write(generate_random_text(file_size_mb))

    if current_depth < depth:
        for d in range(1, width + 1):
            subdir = base_path / f"dir_{current_depth}_{d}"
            subdir.mkdir(parents=True, exist_ok=True)
            create_structure(
                subdir, depth, width, files_per_dir,
                file_size_mb, current_depth + 1
            )


def main():
    if len(sys.argv) != 6:
        print(
            """
            Usage: python3 generate_structure.py
            <depth> <width> <files_per_dir> <file_size_mb> <base_dir>
            """
        )
        sys.exit(1)

    depth = int(sys.argv[1])
    width = int(sys.argv[2])
    files_per_dir = int(sys.argv[3])
    file_size_mb = int(sys.argv[4])
    base_dir = Path(sys.argv[5])

    if base_dir.exists():
        print(f"Cleaning up existing directory: {base_dir}")
        shutil.rmtree(base_dir)
    base_dir.mkdir(parents=True)

    create_structure(base_dir, depth, width, files_per_dir, file_size_mb)
    print(f"Structure created under: {base_dir}")


if __name__ == "__main__":
    main()
