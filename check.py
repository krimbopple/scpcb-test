import pefile
import sys

def is_large_address_aware(exe_path):
    pe = pefile.PE(exe_path)
    IMAGE_FILE_LARGE_ADDRESS_AWARE = 0x20
    return bool(pe.FILE_HEADER.Characteristics & IMAGE_FILE_LARGE_ADDRESS_AWARE)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python check_laa.py <path_to_exe>")
        sys.exit(1)

    exe = sys.argv[1]
    if is_large_address_aware(exe):
        print(f"{exe} IS Large Address Aware")
    else:
        print(f"{exe} is NOT Large Address Aware")
