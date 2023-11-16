const formatTime = (timeline: number[]) => {
  const [y, mo, d, h, mi] = timeline;
  return `${y}. ${mo.toString().padStart(2, '0')}. ${d.toString().padStart(2, '0')}. ${h
    .toString()
    .padStart(2, '0')} : ${mi.toString().padStart(2, '0')}`; // 원하는 형식으로 변환할 수 있습니다.
};

export default formatTime;
