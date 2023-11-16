import Swal from 'sweetalert2';

const inputAlert = (text: string, isError: boolean = true) => {
  return Swal.fire({
    text,
    icon: isError ? 'error' : 'success',
    confirmButtonText: '닫기',
  });
};

export default inputAlert;
